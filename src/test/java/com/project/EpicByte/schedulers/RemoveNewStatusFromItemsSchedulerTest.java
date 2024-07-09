package com.project.EpicByte.schedulers;

import com.project.EpicByte.model.entity.productEntities.*;
import com.project.EpicByte.repository.productRepositories.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RemoveNewStatusFromItemsSchedulerTest {
    @Mock
    private BookRepository bookRepository;
    @Mock
    private TextbookRepository textbookRepository;
    @Mock
    private MovieRepository movieRepository;
    @Mock
    private MusicRepository musicRepository;
    @Mock
    private ToyRepository toyRepository;

    @InjectMocks
    private RemoveNewStatusFromItemsScheduler scheduler;

    @Test
    void removeNewStatusFromItems() {
        // Given
        Book book = new Book();
        Textbook textbook = new Textbook();
        Movie movie = new Movie();
        Music music = new Music();
        Toy toy = new Toy();

        when(bookRepository.findNewBooksOlderThanAWeek(any(LocalDate.class))).thenReturn(Collections.singletonList(book));
        when(textbookRepository.findNewTextbooksOlderThanAWeek(any(LocalDate.class))).thenReturn(Collections.singletonList(textbook));
        when(movieRepository.findNewMoviesOlderThanAWeek(any(LocalDate.class))).thenReturn(Collections.singletonList(movie));
        when(musicRepository.findNewMusicOlderThanAWeek(any(LocalDate.class))).thenReturn(Collections.singletonList(music));
        when(toyRepository.findNewToysOlderThanAWeek(any(LocalDate.class))).thenReturn(Collections.singletonList(toy));

        // When
        scheduler.removeNewStatusFromItems();

        // Then
        verify(bookRepository, times(1)).save(book);
        verify(textbookRepository, times(1)).save(textbook);
        verify(movieRepository, times(1)).save(movie);
        verify(musicRepository, times(1)).save(music);
        verify(toyRepository, times(1)).save(toy);
    }
}