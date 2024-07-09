package com.project.EpicByte.repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class UserRepositoryTest {
    @Mock
    private UserRepository userRepository;
    String username = "testUser";
    UUID id = UUID.randomUUID();

    @Test
    void findUserByUsernameTest() {
        userRepository.findUserByUsername(username);
        verify(userRepository, times(1)).findUserByUsername(username);
    }

    @Test
    void findUserEntityByUsernameTest() {
        userRepository.findUserEntityByUsername(username);
        verify(userRepository, times(1)).findUserEntityByUsername(username);
    }

    @Test
    void findUserEntityByIdTest() {
        userRepository.findUserEntityById(id);
        verify(userRepository, times(1)).findUserEntityById(id);
    }

    @Test
    void existsByUsernameTest() {
        userRepository.existsByUsername(username);
        verify(userRepository, times(1)).existsByUsername(username);
    }

    @Test
    void findAllUsersWithInitializedOrdersTest() {
        userRepository.findAllUsersWithInitializedOrders();
        verify(userRepository, times(1)).findAllUsersWithInitializedOrders();
    }

    @Test
    void findAllUsersWithInitializedCartItemsTest() {
        userRepository.findAllUsersWithInitializedCartItems();
        verify(userRepository, times(1)).findAllUsersWithInitializedCartItems();
    }

    @Test
    void findUserByUsernameWithInitializedCartItemsTest() {
        userRepository.findUserByUsernameWithInitializedCartItems(username);
        verify(userRepository, times(1)).findUserByUsernameWithInitializedCartItems(username);
    }

    @Test
    void findUserByUsernameWithInitializedOrdersTest() {
        userRepository.findUserByUsernameWithInitializedOrders(username);
        verify(userRepository, times(1)).findUserByUsernameWithInitializedOrders(username);
    }

    @Test
    void findUserByIdWithInitializedCartItemsTest() {
        userRepository.findUserByIdWithInitializedCartItems(id);
        verify(userRepository, times(1)).findUserByIdWithInitializedCartItems(id);
    }

    @Test
    void findUserByIdWithInitializedOrdersTest() {
        userRepository.findUserByIdWithInitializedOrders(id);
        verify(userRepository, times(1)).findUserByIdWithInitializedOrders(id);
    }
}