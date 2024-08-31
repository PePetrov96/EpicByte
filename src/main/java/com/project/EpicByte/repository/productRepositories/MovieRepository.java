package com.project.EpicByte.repository.productRepositories;

import com.project.EpicByte.model.entity.BaseProduct;
import com.project.EpicByte.model.entity.productEntities.Book;
import com.project.EpicByte.model.entity.productEntities.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface MovieRepository extends JpaRepository<Movie, UUID> {
    @Query("SELECT m from Movie m WHERE m.id = :userId")
    Movie findMovieById(@Param("userId") UUID userId);

    @Query("SELECT m FROM Movie m WHERE m.isNewProduct = true and m.dateCreated <= :date")
    List<Movie> findNewMoviesOlderThanAWeek(@Param("date") LocalDate date);

    @Query("SELECT m FROM Movie m WHERE m.productName LIKE %:productName%")
    List<BaseProduct> findMoviesByProductName(@Param("productName") String productName);
}
