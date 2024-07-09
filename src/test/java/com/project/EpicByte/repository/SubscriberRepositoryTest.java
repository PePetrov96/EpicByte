package com.project.EpicByte.repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class SubscriberRepositoryTest {

    @Mock
    private SubscriberRepository subscriberRepository;

    @Test
    public void findByEmailTest() {
        String email = "email@example.com";
        subscriberRepository.findByEmail(email);

        verify(subscriberRepository, times(1)).findByEmail(email);
    }
}