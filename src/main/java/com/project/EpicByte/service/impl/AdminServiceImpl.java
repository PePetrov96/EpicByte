package com.project.EpicByte.service.impl;

import com.project.EpicByte.exceptions.UsernameIsEmptyException;
import com.project.EpicByte.model.entity.UserEntity;
import com.project.EpicByte.model.entity.UserRole;
import com.project.EpicByte.repository.UserRepository;
import com.project.EpicByte.repository.UserRoleRepository;
import com.project.EpicByte.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import java.util.*;

import static com.project.EpicByte.model.entity.enums.UserRolesEnum.MODERATOR;
import static com.project.EpicByte.util.Constants.*;

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
        LinkedHashMap<UserEntity, String> userEntityMap = getUserEntityMap();
        model.addAttribute("userMap", userEntityMap);
        return USER_PRIVILEGE_CONTROLLER_HTML;
    }

    @Override
    @Transactional
    public String giveModeratorPrivilegesToUser(UUID id, Model model) {
        UserEntity userEntity = getUserEntityById(id);
        UserRole userRole = this.userRoleRepository.findUserRoleByRole(MODERATOR);
        userEntity.getRoles().add(userRole);
        userRepository.saveAndFlush(userEntity);
        return "redirect:" + ADMIN_MANAGE_PRIVILEGES_URL;
    }

    @Override
    @Transactional
    public String removeModeratorPrivileges(UUID id, Model model) {
        UserEntity userEntity = getUserEntityById(id);
        UserRole userRole = this.userRoleRepository.findUserRoleByRole(MODERATOR);
        userEntity.getRoles().remove(userRole);
        userRepository.saveAndFlush(userEntity);
        return "redirect:" + ADMIN_MANAGE_PRIVILEGES_URL;
    }

    // Support Methods
    @Transactional
    protected UserEntity getUserEntityById(UUID id) {
        Optional<UserEntity> user = this.userRepository.findById(id);

        if (user.isEmpty()) {
            throw new UsernameIsEmptyException();
        }

        return user.get();
    }

    protected LinkedHashMap<UserEntity, String> getUserEntityMap() {
        List<UserEntity> users = this.userRepository.findAll();

        LinkedHashMap<UserEntity, String> userMap = new LinkedHashMap<>();
        UserRole moderatorRole = this.userRoleRepository.findUserRoleByRole(MODERATOR);

        for (UserEntity user : users) {
            String role = "USER";
            Set<UserRole> userRoles = user.getRoles();

            for (UserRole userRole : userRoles) {
                if (userRole.getRole().name().equals(MODERATOR.name())) {
                    role = "MODERATOR";
                    break;
                }
            }

            userMap.put(user, role);
        }

        return userMap;
    }
}