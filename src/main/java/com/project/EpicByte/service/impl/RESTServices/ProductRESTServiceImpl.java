package com.project.EpicByte.service.impl.RESTServices;

import com.project.EpicByte.model.dto.productDTOs.*;
import com.project.EpicByte.model.entity.BaseProduct;
import com.project.EpicByte.model.entity.enums.ProductTypeEnum;
import com.project.EpicByte.model.entity.productEntities.*;
import com.project.EpicByte.repository.*;
import com.project.EpicByte.service.ProductRESTService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class ProductRESTServiceImpl implements ProductRESTService {
    private final BookRepository bookRepository;
    private final TextbookRepository textbookRepository;
    private final MovieRepository movieRepository;
    private final MusicRepository musicRepository;
    private final ToyRepository toyRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public ProductRESTServiceImpl(BookRepository bookRepository,
                                  TextbookRepository textbookRepository,
                                  MovieRepository movieRepository,
                                  MusicRepository musicRepository,
                                  ToyRepository toyRepository,
                                  ModelMapper modelMapper) {
        this.bookRepository = bookRepository;
        this.textbookRepository = textbookRepository;
        this.movieRepository = movieRepository;
        this.musicRepository = musicRepository;
        this.toyRepository = toyRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<? extends BaseProduct> getAll(String productType) {
        return switch (productType) {
            case "BOOKS" -> this.bookRepository.findAll();
            case "TEXTBOOKS" -> this.textbookRepository.findAll();
            case "MOVIES" -> this.movieRepository.findAll();
            case "MUSIC" -> this.musicRepository.findAll();
            case "TOYS" -> this.toyRepository.findAll();
            default -> List.of();
        };
    }

    @Override
    public BaseProduct getProduct(UUID uuid, String productType) {
        return switch (productType) {
            case "BOOKS" -> this.bookRepository.findBookById(uuid);
            case "TEXTBOOKS" -> this.textbookRepository.findTextbookById(uuid);
            case "MOVIES" -> this.movieRepository.findMovieById(uuid);
            case "MUSIC" -> this.musicRepository.findMusicById(uuid);
            case "TOYS" -> this.toyRepository.findToyById(uuid);
            default -> null;
        };
    }

    @Override
    public Book saveBook(BookAddDTO bookAddDTO) {
        Book book = this.modelMapper.map(bookAddDTO, Book.class);

        book.setId(UUID.randomUUID());
        book.setDateCreated(LocalDate.now());
        book.setProductType(ProductTypeEnum.BOOK);

        return this.bookRepository.saveAndFlush(book);
    }

    @Override
    public Textbook saveTextbook(TextbookAddDTO textbookAddDTO) {
        Textbook textbook = this.modelMapper.map(textbookAddDTO, Textbook.class);

        textbook.setId(UUID.randomUUID());
        textbook.setDateCreated(LocalDate.now());
        textbook.setProductType(ProductTypeEnum.TEXTBOOK);

        return this.textbookRepository.saveAndFlush(textbook);
    }

    @Override
    public Music saveMusic(MusicAddDTO musicAddDTO) {
        Music music = this.modelMapper.map(musicAddDTO, Music.class);

        music.setId(UUID.randomUUID());
        music.setDateCreated(LocalDate.now());
        music.setProductType(ProductTypeEnum.MUSIC);

        return this.musicRepository.saveAndFlush(music);
    }

    @Override
    public Movie saveMovie(MovieAddDTO movieAddDTO) {
        Movie movie = this.modelMapper.map(movieAddDTO, Movie.class);

        movie.setId(UUID.randomUUID());
        movie.setDateCreated(LocalDate.now());
        movie.setProductType(ProductTypeEnum.MUSIC);

        return this.movieRepository.saveAndFlush(movie);
    }

    @Override
    public Toy saveToy(ToyAddDTO toyAddDTO) {
        Toy toy = this.modelMapper.map(toyAddDTO, Toy.class);

        toy.setId(UUID.randomUUID());
        toy.setDateCreated(LocalDate.now());
        toy.setProductType(ProductTypeEnum.MUSIC);

        return this.toyRepository.saveAndFlush(toy);
    }

    @Override
    public boolean deleteProduct(UUID uuid, String productType) {
        return switch (productType) {
            case "BOOKS" -> {
                this.bookRepository.deleteById(uuid);
                yield true;
            }
            case "TEXTBOOKS" -> {
                this.textbookRepository.deleteById(uuid);
                yield true;
            }
            case "MOVIES" -> {
                this.movieRepository.deleteById(uuid);
                yield true;
            }
            case "MUSIC" -> {
                this.musicRepository.deleteById(uuid);
                yield true;
            }
            case "TOYS" -> {
                this.toyRepository.deleteById(uuid);
                yield true;
            }
            default -> false;
        };
    }
}
