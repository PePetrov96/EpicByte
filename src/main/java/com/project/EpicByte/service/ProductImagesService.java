package com.project.EpicByte.service;

// CLOUDINARY
public interface ProductImagesService {
    String getImageURL(String imageURL);
    boolean removeImageURL(String imageURL);
}
