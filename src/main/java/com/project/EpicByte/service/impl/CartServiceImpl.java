package com.project.EpicByte.service.impl;

import com.project.EpicByte.exceptions.CartItemNotFoundException;
import com.project.EpicByte.model.dto.productDTOs.CartItemDTO;
import com.project.EpicByte.model.dto.productDTOs.UserCartDTO;
import com.project.EpicByte.model.entity.BaseProduct;
import com.project.EpicByte.model.entity.UserEntity;
import com.project.EpicByte.model.entity.productEntities.*;
import com.project.EpicByte.repository.CartRepository;
import com.project.EpicByte.repository.UserRepository;
import com.project.EpicByte.repository.productRepositories.*;
import com.project.EpicByte.service.CartService;
import com.project.EpicByte.util.Breadcrumbs;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.math.BigDecimal;
import java.util.*;

import static com.project.EpicByte.util.Constants.*;

@Service
public class CartServiceImpl extends Breadcrumbs implements CartService {
    private final BookRepository bookRepository;
    private final TextbookRepository textbookRepository;
    private final MovieRepository movieRepository;
    private final MusicRepository musicRepository;
    private final ToyRepository toyRepository;

    private final CartRepository cartRepository;
    private final UserRepository userRepository;

    public CartServiceImpl(BookRepository bookRepository, TextbookRepository textbookRepository,
                           MovieRepository movieRepository, MusicRepository musicRepository,
                           ToyRepository toyRepository, CartRepository cartRepository, UserRepository userRepository) {
        this.bookRepository = bookRepository;
        this.textbookRepository = textbookRepository;
        this.movieRepository = movieRepository;
        this.musicRepository = musicRepository;
        this.toyRepository = toyRepository;
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
    }



    @Override
    public String showCartPage(String username, Model model) {
        try {
            UserEntity userEntity = getUserEntityByUsername(username);
            setModelAttributesForCart(userEntity, model);
            addProductBreadcrumb(model, USER_CART_URL, "Cart");
        } catch (UsernameNotFoundException e) {
            model.addAttribute("errorType", "Oops...");
            model.addAttribute("errorText", "Something went wrong!");
            return ERROR_PAGE_HTML;
        }
        return CART_HTML;
    }

    @Override
    public String deleteItemFromUserCart(UUID productId, String username, Model model) {
        UserEntity userEntity = getUserEntityByUsername(username);
        List<CartItem> deletionProducts = this.cartRepository.findAllByUserIdAndProductId(userEntity.getId(), productId);

        this.cartRepository.deleteAll(deletionProducts);

        return "redirect:/user/cart";
    }

    @Override
    public String addToCart(UUID productId, String productType, String username, Model model) {
        CartItem cartItem = new CartItem();

        try {
            cartItem.setUser(
                    getUserEntityByUsername(username));

            switch (productType) {
                case "BOOK": cartItem.setProduct(getBookById(productId)); break;
                case "TEXTBOOK": cartItem.setProduct(getTextbookById(productId)); break;
                case "MOVIE": cartItem.setProduct(getMovieById(productId)); break;
                case "MUSIC": cartItem.setProduct(getMusicById(productId)); break;
                case "TOY": cartItem.setProduct(getToyById(productId)); break;
            }

            this.cartRepository.saveAndFlush(cartItem);

        } catch (UsernameNotFoundException | CartItemNotFoundException exception) { //whenever the user is the Admin
            model.addAttribute("errorType", "Oops...");
            model.addAttribute("errorText", "Something went wrong!");
            return ERROR_PAGE_HTML;
        }

        addProductBreadcrumb(model, USER_CART_URL, "Cart");
        return INDEX_HTML;
    }

    //Support methods
    private void setModelAttributesForCart(UserEntity userEntity, Model model) {
        List<BaseProduct> products = this.cartRepository
                .findAllByUserId(userEntity.getId())
                .stream()
                .map(CartItem::getProduct)
                .toList();

        UserCartDTO userCartDTO = new UserCartDTO();

        // Using a map, count the quantity of each unique product
        LinkedHashMap<BaseProduct, Integer> productCountMap = new LinkedHashMap<>();
        for (BaseProduct product : products) {
            productCountMap.put(product, productCountMap.getOrDefault(product, 0) + 1);
        }

        // Convert each unique product to CartItemDTO and set the quantity
        for (BaseProduct product : productCountMap.keySet()) {
            CartItemDTO cartItemDTO = new CartItemDTO();
            int quantity = productCountMap.get(product);
            BigDecimal productPrice = product.getProductPrice();

            cartItemDTO.setId(product.getId());
            cartItemDTO.setProductType(product.getProductType().toString().toLowerCase());
            cartItemDTO.setProductImageUrl(product.getProductImageUrl());
            cartItemDTO.setProductName(product.getProductName());
            cartItemDTO.setProductPrice(product.getProductPrice());
            cartItemDTO.setQuantity(quantity);
            cartItemDTO.setTotalPriceOfProduct(productPrice.multiply(BigDecimal.valueOf(quantity)));

            userCartDTO.getCartItems().add(cartItemDTO);
        }

        // setting the total price of all items in the cart
        BigDecimal totalPrice = userCartDTO.getCartItems().stream()
                .map(CartItemDTO::getTotalPriceOfProduct)
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);

        userCartDTO.setTotalPrice(totalPrice);

        model.addAttribute("cartItemList", userCartDTO);
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

//        Hibernate.initialize(user.getCartItems());

        return user;
    }
}
