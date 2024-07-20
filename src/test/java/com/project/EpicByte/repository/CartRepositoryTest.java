package com.project.EpicByte.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class CartRepositoryTest {
    @Mock
    private CartRepository cartRepository;

    UUID productId;
    UUID userId;

    @BeforeEach
    public void setUp() {
        productId = UUID.randomUUID();
        userId = UUID.randomUUID();
    }

    @Test
    public void findByProductIdTest() {
        cartRepository.findByProductId(productId);
        verify(cartRepository, times(1)).findByProductId(productId);
    }

    @Test
    public void findAllByUserIdTest() {
        cartRepository.findAllByUserId(userId);
        verify(cartRepository, times(1)).findAllByUserId(userId);
    }

    @Test
    public void findAllByUserUsernameTest() {
        String username = "username";
        cartRepository.findAllByUserUsername(username);
        verify(cartRepository, times(1)).findAllByUserUsername(username);
    }

    @Test
    public void findAllByUserIdAndProductIdTest() {
        cartRepository.findAllByUserIdAndProductId(userId, productId);
        verify(cartRepository, times(1)).findAllByUserIdAndProductId(userId, productId);
    }

    @Test
    public void findAllByProductIdTest() {
        cartRepository.findAllByProductId(productId);
        verify(cartRepository, times(1)).findAllByProductId(productId);
    }
}