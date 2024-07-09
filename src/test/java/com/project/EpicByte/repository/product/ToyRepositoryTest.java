package com.project.EpicByte.repository.product;

import com.project.EpicByte.repository.productRepositories.ToyRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.UUID;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ToyRepositoryTest {
    @Mock
    private ToyRepository toyRepository;

    @Test
    void findBookByIdTest() {
        UUID id = UUID.randomUUID();
        toyRepository.findToyById(id);
        verify(toyRepository, times(1)).findToyById(id);
    }

    @Test
    void findNewBooksOlderThanAWeekTest() {
        LocalDate date = LocalDate.now().minusDays(7);
        toyRepository.findNewToysOlderThanAWeek(date);
        verify(toyRepository, times(1)).findNewToysOlderThanAWeek(date);
    }
}
