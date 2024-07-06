package com.project.EpicByte.service.productService;

// CLOUDINARY
public interface ProductImagesService {
    String getImageURL(String imageURL);
    boolean removeImageURL(String imageURL);
}
