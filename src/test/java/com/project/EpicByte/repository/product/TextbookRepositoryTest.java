package com.project.EpicByte.repository.product;

import com.project.EpicByte.repository.productRepositories.TextbookRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.UUID;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class TextbookRepositoryTest {
    @Mock
    private TextbookRepository textbookRepository;

    @Test
    void findBookByIdTest() {
        UUID id = UUID.randomUUID();
        textbookRepository.findTextbookById(id);
        verify(textbookRepository, times(1)).findTextbookById(id);
    }

    @Test
    void findNewBooksOlderThanAWeekTest() {
        LocalDate date = LocalDate.now().minusDays(7);
        textbookRepository.findNewTextbooksOlderThanAWeek(date);
        verify(textbookRepository, times(1)).findNewTextbooksOlderThanAWeek(date);
    }
}
