package com.project.EpicByte.repository.product;

import com.project.EpicByte.repository.productRepositories.MusicRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.UUID;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class MusicRepositoryTest {
    @Mock
    private MusicRepository musicRepository;

    @Test
    void findBookByIdTest() {
        UUID id = UUID.randomUUID();
        musicRepository.findMusicById(id);
        verify(musicRepository, times(1)).findMusicById(id);
    }

    @Test
    void findNewBooksOlderThanAWeekTest() {
        LocalDate date = LocalDate.now().minusDays(7);
        musicRepository.findNewMusicOlderThanAWeek(date);
        verify(musicRepository, times(1)).findNewMusicOlderThanAWeek(date);
    }
}
