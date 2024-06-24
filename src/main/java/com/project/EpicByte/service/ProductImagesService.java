package com.project.EpicByte.service;

// CLOUDINARY
public interface ProductImagesService {
    String getImageURL(String imageURL);
    void removeImageURL(String imageURL);
}
