package com.project.EpicByte.service.productService;

// CLOUDINARY
public interface ProductImagesService {
    String getImageURL(String imageURL);
    void removeImageURL(String imageURL);
}
