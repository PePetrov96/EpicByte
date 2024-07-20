package com.project.EpicByte.schedulers;

import com.project.EpicByte.model.entity.productEntities.*;
import com.project.EpicByte.repository.productRepositories.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class RemoveNewStatusFromItemsScheduler {
    private final BookRepository bookRepository;
    private final TextbookRepository textbookRepository;
    private final MovieRepository movieRepository;
    private final MusicRepository musicRepository;
    private final ToyRepository toyRepository;

    @Autowired
    public RemoveNewStatusFromItemsScheduler(BookRepository bookRepository,
                                             TextbookRepository textbookRepository,
                                             MovieRepository movieRepository,
                                             MusicRepository musicRepository,
                                             ToyRepository toyRepository) {
        this.bookRepository = bookRepository;
        this.textbookRepository = textbookRepository;
        this.movieRepository = movieRepository;
        this.musicRepository = musicRepository;
        this.toyRepository = toyRepository;
    }

    @Scheduled(timeUnit = TimeUnit.HOURS, fixedRate = 24)
    public void removeNewStatusFromItems() {
        log.info("Starting changing statuses of products.");
        removeNewBooks();
        removeNewTextbooks();
        removeNewMovies();
        removeNewMusic();
        removeNewToys();
        log.info("Finished changing statuses of products.");
    }

    private void removeNewToys() {
        List<Toy> toys = toyRepository.findNewToysOlderThanAWeek(LocalDate.now().minusDays(7));
        log.info("Found {} old Toy products.", toys.size());
        for (Toy toy : toys) {
            toy.setNewProduct(false);
            this.toyRepository.save(toy);
        }
    }

    private void removeNewMusic() {
        List<Music> musics = musicRepository.findNewMusicOlderThanAWeek(LocalDate.now().minusDays(7));
        log.info("Found {} old Music products.", musics.size());
        for (Music music : musics) {
            music.setNewProduct(false);
            this.musicRepository.save(music);
        }
    }

    private void removeNewMovies() {
        List<Movie> movies = movieRepository.findNewMoviesOlderThanAWeek(LocalDate.now().minusDays(7));
        log.info("Found {} old Movies products.", movies.size());
        for (Movie movie : movies) {
            movie.setNewProduct(false);
            this.movieRepository.save(movie);
        }
    }

    private void removeNewTextbooks() {
        List<Textbook> textbooks = textbookRepository.findNewTextbooksOlderThanAWeek(LocalDate.now().minusDays(7));
        log.info("Found {} old Textbooks products.", textbooks.size());
        for (Textbook textbook : textbooks) {
            textbook.setNewProduct(false);
            this.textbookRepository.save(textbook);
        }
    }

    private void removeNewBooks() {
        List<Book> books = bookRepository.findNewBooksOlderThanAWeek(LocalDate.now().minusDays(7));
        log.info("Found {} old Books products.", books.size());
        for (Book book : books) {
            book.setNewProduct(false);
            this.bookRepository.save(book);
        }
    }
}
