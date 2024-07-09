package com.project.EpicByte.repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserOrderRepositoryTest {

    @Mock
    private UserOrderRepository userOrderRepository;

    @Test
    void findUserOrderByIdTest() {
        UUID id = UUID.randomUUID();
        userOrderRepository.findUserOrderById(id);

        verify(userOrderRepository, times(1)).findUserOrderById(id);
    }

    @Test
    void findUserOrderByUserIdOrderByOrderDateDescTest() {
        UUID id = UUID.randomUUID();
        userOrderRepository.findUserOrderByUserIdOrderByOrderDateDesc(id);

        verify(userOrderRepository, times(1)).findUserOrderByUserIdOrderByOrderDateDesc(id);
    }

    @Test
    void findUserOrderByUserIdTest() {
        UUID id = UUID.randomUUID();
        userOrderRepository.findUserOrderByUserId(id);

        verify(userOrderRepository, times(1)).findUserOrderByUserId(id);
    }

    @Test
    void findUserOrdersIncompleteOrderByOrderDateDescTest() {
        userOrderRepository.findUserOrdersIncompleteOrderByOrderDateDesc();

        verify(userOrderRepository, times(1)).findUserOrdersIncompleteOrderByOrderDateDesc();
    }

    @Test
    void findUserOrdersIncompleteTest() {
        userOrderRepository.findUserOrdersIncomplete();

        verify(userOrderRepository, times(1)).findUserOrdersIncomplete();
    }
}