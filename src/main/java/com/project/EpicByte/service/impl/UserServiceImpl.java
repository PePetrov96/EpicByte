package com.project.EpicByte.service.impl;

import com.project.EpicByte.model.dto.UserRegisterDTO;
import com.project.EpicByte.model.dto.UserUpdateDTO;
import com.project.EpicByte.model.entity.UserEntity;
import com.project.EpicByte.model.entity.UserRoleEntity;
import com.project.EpicByte.model.entity.enums.UserRolesEnum;
import com.project.EpicByte.repository.UserRepository;
import com.project.EpicByte.repository.UserRoleRepository;
import com.project.EpicByte.service.UserService;
import com.project.EpicByte.validation.UsernameAlreadyExistsException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper,
                           UserRoleRepository userRoleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.userRoleRepository = userRoleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void registerUser(UserRegisterDTO userRegisterDTO) {
        if (this.userRepository.findUserByUsername(userRegisterDTO.getUsername()).isPresent()) {
            throw new UsernameAlreadyExistsException();
        }

        String password = this.passwordEncoder.encode(userRegisterDTO.getPassword());

        UserEntity userEntity = modelMapper.map(userRegisterDTO, UserEntity.class);
        userEntity.setPassword(password);
        userEntity.setRoles(mapUserRoles("USER"));

        this.userRepository.saveAndFlush(userEntity);
    }

    @Override
    public UserUpdateDTO getUserUpdateDTOByUsername(String username) {
        UserEntity userEntity = this.userRepository.findUserEntityByUsername(username);
        return this.modelMapper.map(userEntity, UserUpdateDTO.class);
    }

    @Override
    public void updateUser(UserUpdateDTO userUpdateDTO) {
        UserEntity userEntity = this.userRepository.findUserEntityById(userUpdateDTO.getId());

        userEntity.setFirstName(userUpdateDTO.getFirstName());
        userEntity.setLastName(userUpdateDTO.getLastName());
        userEntity.setUsername(userUpdateDTO.getUsername());
        userEntity.setEmail(userUpdateDTO.getEmail());

        this.userRepository.saveAndFlush(userEntity);
    }

    private Set<UserRoleEntity> mapUserRoles(String role) {
        Set<UserRoleEntity> userRoleEntities = new HashSet<>();

        switch (role) {
            case "ADMIN":
                UserRolesEnum userRolesEnumADMIN = UserRolesEnum.ADMIN;
                UserRoleEntity userRoleEntityADMIN = this.userRoleRepository.findUserRoleByRole(userRolesEnumADMIN);
                userRoleEntities.add(userRoleEntityADMIN);
            case "USER":
                UserRolesEnum userRolesEnumUSER = UserRolesEnum.USER;
                UserRoleEntity userRoleUSEREntity = this.userRoleRepository.findUserRoleByRole(userRolesEnumUSER);
                userRoleEntities.add(userRoleUSEREntity);
        }

        return userRoleEntities;
    }
}
