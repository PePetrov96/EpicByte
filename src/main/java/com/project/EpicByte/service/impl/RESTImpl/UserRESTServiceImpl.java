package com.project.EpicByte.service.impl.RESTImpl;

import com.project.EpicByte.model.dto.RESTDTOs.*;
import com.project.EpicByte.model.entity.UserEntity;
import com.project.EpicByte.model.entity.UserOrder;
import com.project.EpicByte.model.entity.UserRole;
import com.project.EpicByte.model.entity.productEntities.CartItem;
import com.project.EpicByte.repository.CartRepository;
import com.project.EpicByte.repository.UserOrderRepository;
import com.project.EpicByte.repository.UserRepository;
import com.project.EpicByte.service.UserRESTService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserRESTServiceImpl implements UserRESTService {
    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final UserOrderRepository userOrderRepository;
    private final ModelMapper modelMapper;

    public UserRESTServiceImpl(UserRepository userRepository,
                               CartRepository cartRepository,
                               UserOrderRepository userOrderRepository,
                               ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.cartRepository = cartRepository;
        this.userOrderRepository = userOrderRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    @Transactional
    public List<UserRESTViewDTO> getAllUsers() {
        return mapUserEntitiesToUserRESTViewDTOList(
                this.userRepository
                        .findAll());
    }

    @Override
    @Transactional
    public UserRESTViewDTO getUserById(UUID uuid) {
        return convertUserToUserRESTViewDTO(this.userRepository.findUserEntityById(uuid));
    }

    // Support methods
    // Retrieve a list of UserEntities and return a list of UserDTOs
    private List<UserRESTViewDTO> mapUserEntitiesToUserRESTViewDTOList(List<UserEntity> users) {
        List<UserRESTViewDTO> userRESTViewDTOs = new ArrayList<>();
        users.forEach(user -> userRESTViewDTOs.add(convertUserToUserRESTViewDTO(user)));
        return userRESTViewDTOs;
    }

    // Map the UserEntity to UserDTO
    private UserRESTViewDTO convertUserToUserRESTViewDTO(UserEntity user) {
        if (user == null) return null;
        UserRESTViewDTO userRESTViewDTO = this.modelMapper.map(user, UserRESTViewDTO.class);

        userRESTViewDTO.setRoles(mapRoles(user.getRoles()));
        userRESTViewDTO.setCartItems(mapCartItems(user));
        userRESTViewDTO.setUserOrders(mapUserOrders(user));

        return userRESTViewDTO;
    }

    // Map the Roles list
    private HashSet<UserRoleRESTViewDTO> mapRoles(Set<UserRole> roles) {
        return roles.stream()
                .map(userRole -> modelMapper.map(userRole, UserRoleRESTViewDTO.class))
                .collect(Collectors.toCollection(HashSet::new));
    }

    // Map the CartItem list
    private List<CartItemRESTViewDTO> mapCartItems(UserEntity user) {
        List<CartItemRESTViewDTO> cartItemRESTViewDTOs = new ArrayList<>();

        for (CartItem cartItem : user.getCartItems()) {
            CartItemRESTViewDTO cartItemRESTViewDTO = this.modelMapper.map(cartItem, CartItemRESTViewDTO.class);
            cartItemRESTViewDTO.setBaseProduct(this.modelMapper.map(cartItem.getProduct(), BaseProductRESTViewDTO.class));
            cartItemRESTViewDTOs.add(cartItemRESTViewDTO);
        }

        return cartItemRESTViewDTOs;
    }

    // Map the UserOrders list
    private Set<UserOrderRESTViewDTO> mapUserOrders(UserEntity user) {
        Set<UserOrderRESTViewDTO> userOrderDTOList = new HashSet<>();

        for (UserOrder userOrder : user.getUserOrders()) {
            userOrderDTOList .add(
                    this.modelMapper
                            .map(userOrder, UserOrderRESTViewDTO.class));
        }

        return userOrderDTOList;
    }
}
