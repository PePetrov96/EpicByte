package com.project.EpicByte.init;

import com.project.EpicByte.model.entity.UserRoleEntity;
import com.project.EpicByte.model.entity.enums.UserRolesEnum;
import com.project.EpicByte.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * A necessary class for this staging application.
 * It is designed to fill the application with pre-validated data.
 */

@Component
public class DataInitializer implements CommandLineRunner {
    private final UserRoleRepository userRoleRepository;
    private final BookRepository bookRepository;
    private final TextbookRepository textbookRepository;
    private final MovieRepository movieRepository;
    private final MusicRepository musicRepository;
    private final ToyRepository toyRepository;

    @Autowired
    public DataInitializer(UserRoleRepository userRoleRepository, BookRepository bookRepository,
                           TextbookRepository textbookRepository, MovieRepository movieRepository,
                           MusicRepository musicRepository, ToyRepository toyRepository) {
        this.userRoleRepository = userRoleRepository;
        this.bookRepository = bookRepository;
        this.textbookRepository = textbookRepository;
        this.movieRepository = movieRepository;
        this.musicRepository = musicRepository;
        this.toyRepository = toyRepository;
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
