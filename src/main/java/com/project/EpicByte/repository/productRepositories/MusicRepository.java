package com.project.EpicByte.repository.productRepositories;

import com.project.EpicByte.model.entity.BaseProduct;
import com.project.EpicByte.model.entity.productEntities.Book;
import com.project.EpicByte.model.entity.productEntities.Music;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface MusicRepository extends JpaRepository<Music, UUID> {
    @Query("SELECT m from Music m WHERE m.id = :userId")
    Music findMusicById(@Param("userId") UUID userId);

    @Query("SELECT m FROM Music m WHERE m.isNewProduct = true and m.dateCreated <= :date")
    List<Music> findNewMusicOlderThanAWeek(@Param("date") LocalDate date);

    @Query("SELECT m FROM Music m WHERE m.productName LIKE %:productName%")
    List<BaseProduct> findMusicByProductName(@Param("productName") String productName);

    @Query("SELECT m FROM Music m WHERE m.artistName LIKE %:artistName%")
    List<BaseProduct> findMusicByArtistName(@Param("artistName") String artistName);
}
