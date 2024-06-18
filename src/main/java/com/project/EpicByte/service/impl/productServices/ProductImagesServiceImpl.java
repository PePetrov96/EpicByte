package com.project.EpicByte.service.impl.productServices;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.project.EpicByte.repository.ProductImagesRepository;
import com.project.EpicByte.service.ProductImagesService;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

// CLOUDINARY
@Service
public class ProductImagesServiceImpl implements ProductImagesService {
    private final Cloudinary cloudinary;

    private final ProductImagesRepository productImagesRepository;

    @Autowired
    public ProductImagesServiceImpl(ProductImagesRepository productImagesRepository) {
        this.productImagesRepository = productImagesRepository;

        Dotenv dotenv = Dotenv.load();
        this.cloudinary = new Cloudinary(dotenv.get("CLOUDINARY_URL"));
        this.cloudinary.config.secure = true;
    }

    @Override
    public String getImageURL(String imageURL) {
        try {
            Map params1 = ObjectUtils.asMap(
                    "use_filename", true,
                    "unique_filename", false,
                    "overwrite", true
            );

            return cloudinary.uploader().upload(imageURL, params1).get("url").toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
