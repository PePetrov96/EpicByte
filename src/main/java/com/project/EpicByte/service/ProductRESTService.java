package com.project.EpicByte.service;

import com.project.EpicByte.model.dto.productDTOs.*;
import com.project.EpicByte.model.entity.BaseProduct;
import com.project.EpicByte.model.entity.productEntities.*;

import java.util.List;
import java.util.UUID;

public interface ProductRESTService {
    List<? extends BaseProduct> getAll(String productType);
    BaseProduct getProduct(UUID uuid, String productType);
    Book saveBook(BookAddDTO bookAddDTO);
    Textbook saveTextbook(TextbookAddDTO textbookAddDTO);
    Music saveMusic(MusicAddDTO musicAddDTO);
    Movie saveMovie(MovieAddDTO movieAddDTO);
    Toy saveToy(ToyAddDTO toyAddDTO);
    boolean deleteProduct(UUID uuid, String productType);
}
