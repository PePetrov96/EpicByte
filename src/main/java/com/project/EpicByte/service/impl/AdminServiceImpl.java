package com.project.EpicByte.service.impl;

import com.project.EpicByte.exceptions.UsernameIsEmptyException;
import com.project.EpicByte.model.entity.UserEntity;
import com.project.EpicByte.model.entity.UserRoleEntity;
import com.project.EpicByte.repository.UserRepository;
import com.project.EpicByte.repository.UserRoleRepository;
import com.project.EpicByte.service.AdminService;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;

import static com.project.EpicByte.model.entity.enums.UserRolesEnum.MODERATOR;
import static com.project.EpicByte.util.Constants.*;

@Transactional
@Service
public class AdminServiceImpl implements AdminService {
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;

    @Autowired
    public AdminServiceImpl(UserRepository userRepository,
                            UserRoleRepository userRoleRepository) {
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
    }

    @Override
    public String displayAdminManagePrivilegesPage(Model model) {
        LinkedHashMap<String, UserEntity> userEntityMap = getUserEntityMap();
        model.addAttribute("userMap", userEntityMap);
        return USER_PRIVILEGE_CONTROLLER_HTML;
    }

    @Transactional
    @Override
    public String giveModeratorPrivilegesToUser(UUID id, Model model) {
        try {
            UserEntity userEntity = getUserEntityById(id);
            UserRoleEntity userRole = this.userRoleRepository.findUserRoleByRole(MODERATOR);
            userEntity.getRoles().add(userRole);
            userRepository.saveAndFlush(userEntity);
            return "redirect:" + ADMIN_MANAGE_PRIVILEGES_URL;
        } catch (UsernameIsEmptyException e) {
            return returnErrorPage(model);
        }
    }

    @Transactional
    @Override
    public String removeModeratorPrivileges(UUID id, Model model) {
        try {
            UserEntity userEntity = getUserEntityById(id);
            UserRoleEntity userRole = this.userRoleRepository.findUserRoleByRole(MODERATOR);
            userEntity.getRoles().remove(userRole);
            userRepository.saveAndFlush(userEntity);
            return "redirect:" + ADMIN_MANAGE_PRIVILEGES_URL;
        } catch (UsernameIsEmptyException e) {
            return returnErrorPage(model);
        }
    }

    // Support Methods
    protected UserEntity getUserEntityById(UUID id) {
        UserEntity user = this.userRepository
                .findUserEntityById(id);

        if (user == null) {
            throw new UsernameIsEmptyException();
        }

        Hibernate.initialize(user.getCartItems());

        return user;
    }

    private LinkedHashMap<String, UserEntity> getUserEntityMap() {
        List<UserEntity> users = this.userRepository.findAll();

        LinkedHashMap<String, UserEntity> userMap = new LinkedHashMap<>();
        UserRoleEntity moderatorRole = this.userRoleRepository.findUserRoleByRole(MODERATOR);

        for (UserEntity user : users) {
            String role;
            if (user.getRoles().contains(moderatorRole)) {
                role = "MODERATOR";
            } else {
                role = "USER";
            }

            userMap.put(role, user);
        }

        return userMap;
    }

    private String returnErrorPage(Model model) {
        model.addAttribute("errorType", "Oops...");
        model.addAttribute("errorText", "Something went wrong!");
        return ERROR_PAGE_HTML;
    }
}
