package com.project.EpicByte.service.RESTService;

import com.project.EpicByte.model.dto.RESTDTOs.UserRESTViewDTO;
import com.project.EpicByte.model.entity.UserEntity;
import com.project.EpicByte.repository.CartRepository;
import com.project.EpicByte.repository.UserOrderRepository;
import com.project.EpicByte.repository.UserRepository;
import com.project.EpicByte.service.UserRESTService;
import com.project.EpicByte.service.impl.RESTImpl.UserRESTServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserRESTServiceTest {
    UserRESTService userRESTService;

    @Mock
    UserRepository userRepository;
    @Mock
    CartRepository cartRepository;
    @Mock
    UserOrderRepository userOrderRepository;
    ModelMapper modelMapper;

    @BeforeEach
    public void setUp() {
        modelMapper = new ModelMapper();

        this.userRESTService =
                new UserRESTServiceImpl(
                        userRepository,
                        cartRepository,
                        userOrderRepository,
                        modelMapper);
    }

    @Test
    public void getAllUsers_success() {
        boolean allDTOs = this.userRESTService
                .getAllUsers()
                .stream()
                .allMatch(element -> element instanceof UserRESTViewDTO && element != null);
        Assertions.assertTrue(allDTOs);
    }

    @Test
    public void getUserById_success() {
        when(this.userRepository.findUserEntityById(any())).thenReturn(new UserEntity());
        boolean isDTO = this.userRESTService.getUserById(UUID.randomUUID()) instanceof UserRESTViewDTO &&
                this.userRESTService.getUserById(UUID.randomUUID()) != null;
        Assertions.assertTrue(isDTO);
    }
}
