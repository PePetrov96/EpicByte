package com.project.EpicByte.repository.product;

import com.project.EpicByte.repository.productRepositories.BookRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.UUID;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class BookRepositoryTest {
    @Mock
    private BookRepository bookRepository;

    @Test
    void findBookByIdTest() {
        UUID id = UUID.randomUUID();
        bookRepository.findBookById(id);
        verify(bookRepository, times(1)).findBookById(id);
    }

    @Test
    void findNewBooksOlderThanAWeekTest() {
        LocalDate date = LocalDate.now().minusDays(7);
        bookRepository.findNewBooksOlderThanAWeek(date);
        verify(bookRepository, times(1)).findNewBooksOlderThanAWeek(date);
    }
}