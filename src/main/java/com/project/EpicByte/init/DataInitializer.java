package com.project.EpicByte.init;

import com.project.EpicByte.model.entity.UserRole;
import com.project.EpicByte.model.entity.enums.UserRolesEnum;
import com.project.EpicByte.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * A necessary class for this staging application.
 * It is designed to fill the application with the User Roles
 */

@Component
public class DataInitializer implements CommandLineRunner {
    private final UserRoleRepository userRoleRepository;

    @Autowired
    public DataInitializer(UserRoleRepository userRoleRepository) {
        this.userRoleRepository = userRoleRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (userRoleRepository.count() == 0) {
            fillUserRoles();
        }
    }

    private void fillUserRoles() {
        //SAVE USER ROLE
        UserRole userRoleUSEREntity = new UserRole();
        userRoleUSEREntity.setRole(UserRolesEnum.USER);
        this.userRoleRepository.save(userRoleUSEREntity);

        //SAVE MODERATOR ROLE
        UserRole userRoleMODERATOR = new UserRole();
        userRoleMODERATOR.setRole(UserRolesEnum.MODERATOR);
        this.userRoleRepository.save(userRoleMODERATOR);

        //SAVE ADMIN ROLE
        UserRole userRoleADMIN = new UserRole();
        userRoleADMIN.setRole(UserRolesEnum.ADMIN);
        this.userRoleRepository.save(userRoleADMIN);
    }
}
