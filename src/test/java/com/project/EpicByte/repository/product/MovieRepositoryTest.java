package com.project.EpicByte.repository.product;

import com.project.EpicByte.repository.productRepositories.MovieRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.UUID;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class MovieRepositoryTest {
    @Mock
    private MovieRepository movieRepository;

    @Test
    void findBookByIdTest() {
        UUID id = UUID.randomUUID();
        movieRepository.findMovieById(id);
        verify(movieRepository, times(1)).findMovieById(id);
    }

    @Test
    void findNewBooksOlderThanAWeekTest() {
        LocalDate date = LocalDate.now().minusDays(7);
        movieRepository.findNewMoviesOlderThanAWeek(date);
        verify(movieRepository, times(1)).findNewMoviesOlderThanAWeek(date);
    }
}
