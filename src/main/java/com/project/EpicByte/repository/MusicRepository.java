package com.project.EpicByte.repository;

import com.project.EpicByte.model.entity.productEntities.Music;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MusicRepository extends JpaRepository<Music, Long> {
}
