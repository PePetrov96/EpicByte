package com.project.EpicByte.service.impl;

import com.project.EpicByte.model.dto.UserRegisterDTO;
import com.project.EpicByte.model.dto.UserUpdateDTO;
import com.project.EpicByte.model.entity.userEntities.UserEntity;
import com.project.EpicByte.model.entity.userEntities.UserRoleEntity;
import com.project.EpicByte.model.entity.enums.UserRolesEnum;
import com.project.EpicByte.repository.UserRepository;
import com.project.EpicByte.repository.UserRoleRepository;
import com.project.EpicByte.service.UserService;
import com.project.EpicByte.util.Breadcrumbs;
import com.project.EpicByte.exceptions.UsernameAlreadyExistsException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.HashSet;
import java.util.Set;

import static com.project.EpicByte.util.Constants.*;

@Service
public class UserServiceImpl extends Breadcrumbs implements UserService{
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;

    // Defining constants


    @Autowired
    public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper, UserRoleRepository userRoleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.userRoleRepository = userRoleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public String showLoginPage() {
        return AUTH_LOGIN_HTML;
    }

    @Override
    public String handleLoginError(Model model) {
        model.addAttribute("loginError", true);
        return AUTH_LOGIN_HTML;
    }

    @Override
    public String showRegisterPage(Model model) {
        model.addAttribute(USER_REGISTER_DTO, new UserRegisterDTO());
        return AUTH_REGISTER_HTML;
    }

    @Override
    public String registerUser(UserRegisterDTO userRegisterDTO, Model model, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return AUTH_REGISTER_HTML;
        }

        saveUserInDatabase(userRegisterDTO);
        return "redirect:/users/login";
    }

    @Override
    public String showProfilePage(Model model, Principal principal) {
        addProductBreadcrumb(model, USERS_PROFILE_URL, "User Profile");

        // Populate user values in the fields
        try {
            UserUpdateDTO userUpdateDTO = this.getUserUpdateDTOByUsername(principal.getName());
            model.addAttribute(USER_UPDATE_DTO, userUpdateDTO);
        } catch (UsernameNotFoundException e) {
            model.addAttribute("errorType", "Oops...");
            model.addAttribute("errorText", "Something went wrong!");
            return ERROR_PAGE_HTML;
        }

        // check if it is a redirect with previously successful operation or a new request
        if (model.containsAttribute("operationSuccess")) {
            model.addAttribute("operationSuccess", true);
        } else {
            model.addAttribute("operationSuccess", false);
        }

        return USER_PROFILE_HTML;
    }

    @Override
    public String updateProfilePage(UserUpdateDTO userUpdateDTO, Model model, RedirectAttributes redirectAttributes, Principal principal) {
        BindingResult bindingResult = new BeanPropertyBindingResult(userUpdateDTO, "userUpdateDTO");

        if (bindingResult.hasErrors()) {
            addProductBreadcrumb(model, USERS_PROFILE_URL, "User Profile");
            return USER_PROFILE_HTML;
        }

        try {
            updateUserInDatabase(userUpdateDTO);
            redirectAttributes.addFlashAttribute("operationSuccess", true);
            return "redirect:" + USERS_PROFILE_URL;
        } catch (UsernameAlreadyExistsException e) {
            bindingResult.rejectValue(USERNAME_FIELD, "user.userUpdateDTO", e.getMessage());
            addProductBreadcrumb(model, USERS_PROFILE_URL, "User Profile");
            return USER_PROFILE_HTML;
        }
    }

    private void updateUserInDatabase(UserUpdateDTO userUpdateDTO) {
        UserEntity existingUser = this.userRepository
                .findUserEntityByUsername(userUpdateDTO.getUsername());

        if (existingUser != null && !existingUser.getId().equals(userUpdateDTO.getId())) {
            throw new UsernameAlreadyExistsException("Username already exists");
        }

        UserEntity userEntity = this.userRepository.findUserEntityById(userUpdateDTO.getId());

        userEntity.setFirstName(userUpdateDTO.getFirstName());
        userEntity.setLastName(userUpdateDTO.getLastName());
        userEntity.setUsername(userUpdateDTO.getUsername());
        userEntity.setEmail(userUpdateDTO.getEmail());

        this.userRepository.saveAndFlush(userEntity);
    }

    private void saveUserInDatabase(UserRegisterDTO userRegisterDTO) {
        String password = this.passwordEncoder.encode(userRegisterDTO.getPassword());

        UserEntity userEntity = modelMapper.map(userRegisterDTO, UserEntity.class);
        userEntity.setPassword(password);
        userEntity.setRoles(mapUserRoles("USER"));

        this.userRepository.saveAndFlush(userEntity);
    }

    private UserUpdateDTO getUserUpdateDTOByUsername(String username) {
        UserEntity userEntity = this.userRepository.findUserEntityByUsername(username);

        if (userEntity == null) {
            throw new UsernameNotFoundException(username);
        }

        return this.modelMapper.map(userEntity, UserUpdateDTO.class);
    }

    private Set<UserRoleEntity> mapUserRoles(String role) {
        Set<UserRoleEntity> userRoleEntities = new HashSet<>();

        switch (role) {
            case "ADMIN":
                UserRoleEntity userRoleEntityADMIN = this.userRoleRepository.findUserRoleByRole(UserRolesEnum.ADMIN);
                userRoleEntities.add(userRoleEntityADMIN);
            case "USER":
                UserRoleEntity userRoleUSEREntity = this.userRoleRepository.findUserRoleByRole(UserRolesEnum.USER);
                userRoleEntities.add(userRoleUSEREntity);
        }

        return userRoleEntities;
    }
}
