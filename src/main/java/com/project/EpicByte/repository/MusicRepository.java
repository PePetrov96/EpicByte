package com.project.EpicByte.repository;

import com.project.EpicByte.model.entity.productEntities.Music;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MusicRepository extends JpaRepository<Music, UUID> {
    @Query("SELECT m from Music m WHERE m.id = :userId")
    Music findMusicById(@Param("userId") UUID userId);
}
