package com.project.EpicByte.service.impl;

import com.project.EpicByte.exceptions.EmptyCartException;
import com.project.EpicByte.model.bindingModel.CartItemBindingModel;
import com.project.EpicByte.model.bindingModel.UserCartBindingModel;
import com.project.EpicByte.model.entity.BaseProduct;
import com.project.EpicByte.model.entity.UserEntity;
import com.project.EpicByte.model.entity.productEntities.*;
import com.project.EpicByte.repository.CartRepository;
import com.project.EpicByte.repository.UserRepository;
import com.project.EpicByte.repository.productRepositories.*;
import com.project.EpicByte.service.CartService;
import com.project.EpicByte.util.Breadcrumbs;
import jakarta.servlet.http.HttpSession;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.project.EpicByte.util.Constants.CART_HTML;
import static com.project.EpicByte.util.Constants.USER_CART_URL;

/**
 * By far the most complex controller, due to the fact that we have to account for multiple things like adding
 * different product types, from different repositories, into the same place. Saving the cart items for later use.
 * Updating the cart count without extending the complexity of the server requests and many more details to account for.
 */

@Service
public class CartServiceImpl implements CartService {
    private final BookRepository bookRepository;
    private final TextbookRepository textbookRepository;
    private final MovieRepository movieRepository;
    private final MusicRepository musicRepository;
    private final ToyRepository toyRepository;

    private final CartRepository cartRepository;
    private final UserRepository userRepository;

    private final ModelMapper modelMapper;

    private final Breadcrumbs breadcrumbs;

    public CartServiceImpl(BookRepository bookRepository,
                           TextbookRepository textbookRepository,
                           MovieRepository movieRepository,
                           MusicRepository musicRepository,
                           ToyRepository toyRepository,
                           CartRepository cartRepository,
                           UserRepository userRepository,
                           ModelMapper modelMapper,
                           Breadcrumbs breadcrumbs) {
        this.bookRepository = bookRepository;
        this.textbookRepository = textbookRepository;
        this.movieRepository = movieRepository;
        this.musicRepository = musicRepository;
        this.toyRepository = toyRepository;
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.breadcrumbs = breadcrumbs;
    }

    @Override
    public String showCartPage(String username, Model model, HttpSession session) {
        try {
            UserEntity userEntity = getUserEntityByUsername(username);
            setModelAttributesForCart(userEntity, model, session);
            breadcrumbs.addProductBreadcrumb(model, USER_CART_URL, "Cart");
        } catch (EmptyCartException e) {
            return returnEmptyCartPage(model);
        }

        return CART_HTML;
    }

    @Override
    @Transactional
    public String deleteItemFromUserCart(UUID productId, String username, Model model, HttpSession session) {
        UserEntity userEntity = getUserEntityByUsername(username);
        List<CartItem> deletionProducts = this.cartRepository
                .findAllByUserIdAndProductId(userEntity.getId(), productId);

        for (CartItem cartItem : deletionProducts) {
            userEntity.getCartItems().remove(cartItem);
            this.cartRepository.delete(cartItem);
        }


        int numItems = (int) session.getAttribute("numItems") - deletionProducts.size();
        session.setAttribute("numItems", numItems);

        return "redirect:" + USER_CART_URL;
    }

    @Override
    public ResponseEntity<String> addProductToCart(Map<String, Object> payload, Principal principal, HttpSession session) {
        try {
            UUID productId = UUID.fromString(payload.get("productId").toString());
            String productType = payload.get("productType").toString();
            addToCart(productId, productType, principal, session);
            return ResponseEntity.ok("Product added to cart successfully");
        } catch (UsernameNotFoundException exception) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error while adding product to cart");
        }
    }

    private String returnEmptyCartPage(Model model) {
        model.addAttribute("emptyCart", true);
        breadcrumbs.addProductBreadcrumb(model, USER_CART_URL, "Cart");
        return CART_HTML;
    }

    public void addToCart(UUID productId, String productType, Principal principal, HttpSession session) {
        CartItem cartItem = new CartItem();

        if (principal == null) {
            throw new UsernameNotFoundException("Username not found.");
        }

        UserEntity userEntity = getUserEntityByUsername(principal.getName());

        cartItem.setUser(userEntity);

        switch (productType) {
            case "BOOK": cartItem.setProduct(getBookById(productId)); break;
            case "TEXTBOOK": cartItem.setProduct(getTextbookById(productId)); break;
            case "MOVIE": cartItem.setProduct(getMovieById(productId)); break;
            case "MUSIC": cartItem.setProduct(getMusicById(productId)); break;
            case "TOY": cartItem.setProduct(getToyById(productId)); break;
        }

        this.cartRepository.saveAndFlush(cartItem);
        int numItems = (int) session.getAttribute("numItems");
        session.setAttribute("numItems", numItems + 1);
    }

    private void setModelAttributesForCart(UserEntity userEntity, Model model, HttpSession session) {
        List<BaseProduct> products = this.cartRepository
                .findAllByUserId(userEntity.getId())
                .stream()
                .map(CartItem::getProduct)
                .toList();

        if (products.isEmpty()) {
            throw new EmptyCartException();
        }

        UserCartBindingModel userCartBindingModel = new UserCartBindingModel();

        // Using a map, count the quantity of each unique product
        LinkedHashMap<BaseProduct, Integer> productCountMap = new LinkedHashMap<>();
        for (BaseProduct product : products) {
            productCountMap.put(product, productCountMap.getOrDefault(product, 0) + 1);
        }

        // Convert each unique product to CartItemDTO and set the quantity
        for (BaseProduct product : productCountMap.keySet()) {
            CartItemBindingModel cartItemBindingModel = this.modelMapper.map(product, CartItemBindingModel.class);
            cartItemBindingModel.setProductType(product.getProductType().toString().toLowerCase());
            int quantity = productCountMap.get(product);
            cartItemBindingModel.setQuantity(quantity);
            BigDecimal productPrice = product.getProductPrice();
            cartItemBindingModel.setTotalPriceOfProduct(productPrice.multiply(BigDecimal.valueOf(quantity)));
            userCartBindingModel.getCartItems().add(cartItemBindingModel);
        }

        // setting the total price of all items in the cart
        BigDecimal totalPrice = userCartBindingModel.getCartItems().stream()
                .map(CartItemBindingModel::getTotalPriceOfProduct)
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);

        userCartBindingModel.setTotalPrice(totalPrice);

        model.addAttribute("cartItemList", userCartBindingModel);
        session.setAttribute("userCartBindingModel", userCartBindingModel);
    }

    private Book getBookById(UUID productId) {
        return this.bookRepository.findBookById(productId);
    }

    private Textbook getTextbookById(UUID productId) {
        return this.textbookRepository.findTextbookById(productId);
    }

    private Movie getMovieById(UUID productId) {
        return this.movieRepository.findMovieById(productId);
    }

    private Music getMusicById(UUID productId) {
        return this.musicRepository.findMusicById(productId);
    }

    private Toy getToyById(UUID productId) {
        return this.toyRepository.findToyById(productId);
    }

    private UserEntity getUserEntityByUsername(String username) {
        UserEntity user = this.userRepository
                .findUserEntityByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException(username);
        }

        return user;
    }
}
