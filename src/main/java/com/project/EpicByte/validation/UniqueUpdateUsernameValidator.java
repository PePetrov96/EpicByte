package com.project.EpicByte.validation;

import com.project.EpicByte.model.dto.UserUpdateDTO;
import com.project.EpicByte.model.entity.UserEntity;
import com.project.EpicByte.repository.UserRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

public class UniqueUpdateUsernameValidator implements ConstraintValidator<UniqueUpdateUsername, UserUpdateDTO> {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void initialize(UniqueUpdateUsername constraint) {
    }

    @Override
    public boolean isValid(UserUpdateDTO user, ConstraintValidatorContext context) {
        UserEntity userEntity = userRepository.findUserEntityByUsername(user.getUsername());

        if(userEntity != null && !userEntity.getId().equals(user.getId())) {
            context.disableDefaultConstraintViolation();

            context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                    .addPropertyNode("username").addConstraintViolation();

            return false;
        }
        return true;
    }
}
