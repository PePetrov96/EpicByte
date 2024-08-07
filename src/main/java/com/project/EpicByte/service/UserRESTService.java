package com.project.EpicByte.service;

import com.project.EpicByte.model.dto.RESTDTOs.UserRESTViewDTO;

import java.util.List;
import java.util.UUID;

public interface UserRESTService {
    List<UserRESTViewDTO> getAllUsers();
    UserRESTViewDTO getUserById(UUID uuid);
}
