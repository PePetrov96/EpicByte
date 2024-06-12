package com.project.EpicByte.repository;

import com.project.EpicByte.model.entity.productEntities.Book;
import com.project.EpicByte.model.entity.productEntities.Music;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MusicRepository extends JpaRepository<Music, Long> {
    @Query("SELECT m from Music m WHERE m.id = :userId")
    Music findMusicById(@Param("userId") Long userId);
}
