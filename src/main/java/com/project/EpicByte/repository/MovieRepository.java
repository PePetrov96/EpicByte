package com.project.EpicByte.repository;

import com.project.EpicByte.model.entity.productEntities.Book;
import com.project.EpicByte.model.entity.productEntities.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
    @Query("SELECT m from Movie m WHERE m.id = :userId")
    Movie findMovieById(@Param("userId") Long userId);
}
