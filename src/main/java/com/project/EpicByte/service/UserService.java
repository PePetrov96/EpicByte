package com.project.EpicByte.service;

import com.project.EpicByte.model.dto.UserRegisterDTO;
import com.project.EpicByte.model.dto.UserUpdateDTO;

public interface UserService {
    void registerUser(UserRegisterDTO userRegisterDTO);
    UserUpdateDTO getUserUpdateDTOByUsername(String username);
    void updateUser(UserUpdateDTO userUpdateDTO);
}
