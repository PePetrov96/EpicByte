package com.project.EpicByte.service.RESTService;

import com.project.EpicByte.model.entity.productEntities.*;
import com.project.EpicByte.repository.CartRepository;
import com.project.EpicByte.repository.productRepositories.*;
import com.project.EpicByte.service.impl.RESTImpl.ProductRESTServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductRESTServiceTest {
    @Mock
    BookRepository bookRepository;
    @Mock
    TextbookRepository textbookRepository;
    @Mock
    MovieRepository movieRepository;
    @Mock
    MusicRepository musicRepository;
    @Mock
    ToyRepository toyRepository;
    @Mock
    CartRepository cartRepository;

    ProductRESTService productRESTService;
    ModelMapper modelMapper;

    @BeforeEach
    public void setUp() {
        modelMapper = new ModelMapper();

        this.productRESTService =
                new ProductRESTServiceImpl(
                        bookRepository,
                        textbookRepository,
                        movieRepository,
                        musicRepository,
                        toyRepository,
                        cartRepository,
                        modelMapper);
    }

    @Test
    public void getAll_success_allBookObjects() {
        boolean allBooks = this.productRESTService
                .getAll("BOOK")
                .stream()
                .allMatch(element -> element instanceof Book);
        Assertions.assertTrue(allBooks);
    }

    @Test
    public void getProduct_success_bookObject() {
        when(this.bookRepository.findBookById(any())).thenReturn(new Book());
        boolean isBook = this.productRESTService.getProduct(UUID.randomUUID(), "BOOKS") instanceof Book;
        Assertions.assertTrue(isBook);
    }

    @Test
    public void deleteProduct_success_BookObject() {
        boolean deleted = this.productRESTService.deleteProduct(UUID.randomUUID(), "BOOKS");
        Assertions.assertTrue(deleted);
    }


    @Test
    public void getAll_success_allTextbookObjects() {
        boolean allTextbooks = this.productRESTService
                .getAll("TEXTBOOK")
                .stream()
                .allMatch(element -> element instanceof Textbook);
        Assertions.assertTrue(allTextbooks);
    }

    @Test
    public void getProduct_success_TextbookObject() {
        when(this.textbookRepository.findTextbookById(any())).thenReturn(new Textbook());
        boolean isTextbook = this.productRESTService.getProduct(UUID.randomUUID(), "TEXTBOOKS") instanceof Textbook;
        Assertions.assertTrue(isTextbook);
    }

    @Test
    public void deleteProduct_success_TextbookObject() {
        boolean deleted = this.productRESTService.deleteProduct(UUID.randomUUID(), "TEXTBOOKS");
        Assertions.assertTrue(deleted);
    }


    @Test
    public void getAll_success_allMovieObjects() {
        boolean allMovies = this.productRESTService
                .getAll("MOVIE")
                .stream()
                .allMatch(element -> element instanceof Movie);
        Assertions.assertTrue(allMovies);
    }

    @Test
    public void getProduct_success_MovieObject() {
        when(this.movieRepository.findMovieById(any())).thenReturn(new Movie());
        boolean isMovie = this.productRESTService.getProduct(UUID.randomUUID(), "MOVIES") instanceof Movie;
        Assertions.assertTrue(isMovie);
    }

    @Test
    public void deleteProduct_success_MovieObject() {
        boolean deleted = this.productRESTService.deleteProduct(UUID.randomUUID(), "MOVIES");
        Assertions.assertTrue(deleted);
    }


    @Test
    public void getAll_success_allMusicObjects() {
        boolean allMusic = this.productRESTService
                .getAll("MUSIC")
                .stream()
                .allMatch(element -> element instanceof Book);
        Assertions.assertTrue(allMusic);
    }

    @Test
    public void getProduct_success_MusicObject() {
        when(this.musicRepository.findMusicById(any())).thenReturn(new Music());
        boolean isMusic = this.productRESTService.getProduct(UUID.randomUUID(), "MUSIC") instanceof Music;
        Assertions.assertTrue(isMusic);
    }

    @Test
    public void deleteProduct_success_MusicObject() {
        boolean deleted = this.productRESTService.deleteProduct(UUID.randomUUID(), "MUSIC");
        Assertions.assertTrue(deleted);
    }


    @Test
    public void getAll_success_allToyObjects() {
        boolean allToys = this.productRESTService
                .getAll("TOY")
                .stream()
                .allMatch(element -> element instanceof Toy);
        Assertions.assertTrue(allToys);
    }

    @Test
    public void getProduct_success_ToyObject() {
        when(this.toyRepository.findToyById(any())).thenReturn(new Toy());
        boolean isToy = this.productRESTService.getProduct(UUID.randomUUID(), "TOYS") instanceof Toy;
        Assertions.assertTrue(isToy);
    }

    @Test
    public void deleteProduct_success_ToyObject() {
        boolean deleted = this.productRESTService.deleteProduct(UUID.randomUUID(), "TOYS");
        Assertions.assertTrue(deleted);
    }
}
