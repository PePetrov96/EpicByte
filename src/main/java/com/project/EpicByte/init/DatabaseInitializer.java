package com.project.EpicByte.init;

import com.project.EpicByte.model.entity.User;
import com.project.EpicByte.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DatabaseInitializer implements CommandLineRunner {
    private final UserRepository userRepository;

    @Autowired
    public DatabaseInitializer(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (this.userRepository.count() == 0) {
            initializeSomeEntities();
        }
    }

    private void initializeSomeEntities() {
        User user = new User();
        user.setUsername("PePetrov96");
        this.userRepository.save(user);
    }
}
