package com.project.EpicByte.service.impl;

import com.project.EpicByte.events.ChangeUsernameEvent;
import com.project.EpicByte.events.UserRegisterEvent;
import com.project.EpicByte.exceptions.LogoutRequestException;
import com.project.EpicByte.model.dto.UserRegisterDTO;
import com.project.EpicByte.model.dto.UserUpdateDTO;
import com.project.EpicByte.model.entity.UserEntity;
import com.project.EpicByte.model.entity.UserRole;
import com.project.EpicByte.model.entity.enums.UserRolesEnum;
import com.project.EpicByte.repository.UserRepository;
import com.project.EpicByte.repository.UserRoleRepository;
import com.project.EpicByte.service.UserService;
import com.project.EpicByte.util.Breadcrumbs;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.HashSet;
import java.util.Set;

import static com.project.EpicByte.util.Constants.*;

@Service
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;
    private final HttpServletRequest request;
    private final ApplicationEventPublisher eventPublisher;
    private final Breadcrumbs breadcrumbs;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           ModelMapper modelMapper,
                           UserRoleRepository userRoleRepository,
                           PasswordEncoder passwordEncoder,
                           HttpServletRequest request,
                           ApplicationEventPublisher eventPublisher,
                           Breadcrumbs breadcrumbs) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.userRoleRepository = userRoleRepository;
        this.passwordEncoder = passwordEncoder;
        this.request = request;
        this.eventPublisher = eventPublisher;
        this.breadcrumbs = breadcrumbs;
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
    public String registerUser(UserRegisterDTO userRegisterDTO, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return AUTH_REGISTER_HTML;
        }

        saveUserInDatabase(userRegisterDTO);

        this.eventPublisher.publishEvent(
                new UserRegisterEvent(
                        this,
                        userRegisterDTO.getUsername(),
                        userRegisterDTO.getEmail(),
                        userRegisterDTO.getFirstName() + " " + userRegisterDTO.getLastName()));

        return "redirect:" + LOGIN_URL;
    }

    @Override
    public String showProfilePage(Model model, Principal principal) {
        breadcrumbs.addProductBreadcrumb(model, USER_PROFILE_URL, "Profile");

        try {
            UserUpdateDTO userUpdateDTO = this.getUserUpdateDTOByUsername(principal.getName());
            model.addAttribute(USER_UPDATE_DTO, userUpdateDTO);
        } catch (UsernameNotFoundException e) {
            throw new UsernameNotFoundException("Username not found");
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
    public String updateProfilePage(UserUpdateDTO userUpdateDTO,
                                    BindingResult bindingResult,
                                    Model model,
                                    RedirectAttributes redirectAttributes,
                                    Principal principal) {
        if (bindingResult.hasErrors()) {
            breadcrumbs.addProductBreadcrumb(model, USER_PROFILE_URL, "Profile");
            return USER_PROFILE_HTML;
        }

        try {
            updateUserInDatabase(userUpdateDTO);
            redirectAttributes.addFlashAttribute("operationSuccess", true);
            return "redirect:" + USER_PROFILE_URL;
        } catch (LogoutRequestException e) {
            return "redirect:" + LOGIN_URL;
        }
    }

    //Support methods
    private void updateUserInDatabase(UserUpdateDTO userUpdateDTO) {
        UserEntity userEntity = this.userRepository.findUserEntityById(userUpdateDTO.getId());

        userEntity.setFirstName(userUpdateDTO.getFirstName());
        userEntity.setLastName(userUpdateDTO.getLastName());
        userEntity.setEmail(userUpdateDTO.getEmail());

        if (!userEntity.getUsername().equals(userUpdateDTO.getUsername())) {
            this.eventPublisher.publishEvent(
                    new ChangeUsernameEvent(
                            this,
                            userEntity.getUsername(),
                            userUpdateDTO.getUsername(),
                            userEntity.getEmail(),
                            userEntity.getFirstName() + " " + userEntity.getLastName()));

            userEntity.setUsername(userUpdateDTO.getUsername());
            this.userRepository.saveAndFlush(userEntity);

            try {
                request.logout();
                throw new LogoutRequestException();
            } catch (ServletException e) {
                throw new RuntimeException();
            }
        }

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

    private Set<UserRole> mapUserRoles(String role) {
        Set<UserRole> userRoleEntities = new HashSet<>();

        switch (role) {
            case "MODERATOR":
                UserRole userRoleMODERATOR = this.userRoleRepository.findUserRoleByRole(UserRolesEnum.MODERATOR);
                userRoleEntities.add(userRoleMODERATOR);
            case "USER":
                UserRole userRoleUSEREntity = this.userRoleRepository.findUserRoleByRole(UserRolesEnum.USER);
                userRoleEntities.add(userRoleUSEREntity);
        }

        return userRoleEntities;
    }
}
