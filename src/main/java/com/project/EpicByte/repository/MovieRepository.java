package com.project.EpicByte.repository;

import com.project.EpicByte.model.entity.productEntities.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MovieRepository extends JpaRepository<Movie, UUID> {
    @Query("SELECT m from Movie m WHERE m.id = :userId")
    Movie findMovieById(@Param("userId") UUID userId);
}
