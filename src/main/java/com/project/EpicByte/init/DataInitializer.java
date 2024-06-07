package com.project.EpicByte.init;

import com.project.EpicByte.model.entity.UserRoleEntity;
import com.project.EpicByte.model.entity.enums.UserRolesEnum;
import com.project.EpicByte.repository.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

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
            //SAVE USER ROLE
            UserRoleEntity userRoleUSEREntity = new UserRoleEntity();
            userRoleUSEREntity.setRole(UserRolesEnum.USER);
            this.userRoleRepository.save(userRoleUSEREntity);

            //SAVE ADMIN ROLE
            UserRoleEntity userRoleEntityADMIN = new UserRoleEntity();
            userRoleEntityADMIN.setRole(UserRolesEnum.ADMIN);
            this.userRoleRepository.save(userRoleEntityADMIN);
        }
    }
}
