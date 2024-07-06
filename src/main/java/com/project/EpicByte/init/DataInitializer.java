package com.project.EpicByte.init;

import com.project.EpicByte.model.entity.UserRoleEntity;
import com.project.EpicByte.model.entity.enums.UserRolesEnum;
import com.project.EpicByte.repository.*;
import com.project.EpicByte.repository.productRepositories.*;
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
        UserRoleEntity userRoleUSEREntity = new UserRoleEntity();
        userRoleUSEREntity.setRole(UserRolesEnum.USER);
        this.userRoleRepository.save(userRoleUSEREntity);

        //SAVE MODERATOR ROLE
        UserRoleEntity userRoleEntityMODERATOR = new UserRoleEntity();
        userRoleEntityMODERATOR.setRole(UserRolesEnum.MODERATOR);
        this.userRoleRepository.save(userRoleEntityMODERATOR);

        //SAVE ADMIN ROLE
        UserRoleEntity userRoleEntityADMIN = new UserRoleEntity();
        userRoleEntityADMIN.setRole(UserRolesEnum.ADMIN);
        this.userRoleRepository.save(userRoleEntityADMIN);
    }
}
