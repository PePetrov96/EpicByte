package com.project.EpicByte.service.impl;

import com.project.EpicByte.model.dto.MusicAddDTO;
import com.project.EpicByte.model.dto.ToyAddDTO;
import com.project.EpicByte.model.entity.enums.LanguageEnum;
import com.project.EpicByte.model.entity.enums.MusicCarrierEnum;
import com.project.EpicByte.model.entity.enums.ProductTypeEnum;
import com.project.EpicByte.model.entity.productEntities.Music;
import com.project.EpicByte.model.entity.productEntities.Toy;
import com.project.EpicByte.repository.ToyRepository;
import com.project.EpicByte.service.ToyService;
import com.project.EpicByte.util.FieldNamesGenerator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import static com.project.EpicByte.util.Constants.DISPLAY_TEXT_HTML;
import static com.project.EpicByte.util.Constants.PRODUCT_ADD_HTML;

@Service
public class ToyServiceImpl extends FieldNamesGenerator implements ToyService {
    private final ToyRepository toyRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public ToyServiceImpl(ToyRepository toyRepository, ModelMapper modelMapper) {
        this.toyRepository = toyRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public String displayProductAddToyPage(Model model) {
        model.addAttribute("productType", "toy");
        model.addAttribute("product", new ToyAddDTO());
        model.addAttribute("fieldsMap", getFieldNames("toy"));
//        model.addAttribute("enumsList", LanguageEnum.values());
        return PRODUCT_ADD_HTML;
    }

    @Override
    public String handleProductAddToy(ToyAddDTO toyAddDTO, BindingResult bindingResult, Model model) {
        model.addAttribute("productType", "toy");

        if (bindingResult.hasErrors()) {
            model.addAttribute("fieldsMap", getFieldNames("toy"));
//            model.addAttribute("enumsList", LanguageEnum.values());
            return PRODUCT_ADD_HTML;
        }

        addToyToDatabase(toyAddDTO);

        model.addAttribute("pageType", "Completed Successfully");
        model.addAttribute("pageText", "Toy added successfully!");
        return DISPLAY_TEXT_HTML;
    }

    // Support methods
    private void addToyToDatabase(ToyAddDTO toyAddDTO) {
        Toy toy = modelMapper.map(toyAddDTO, Toy.class);

        toy.setProductType(ProductTypeEnum.TOY);
        toyRepository.saveAndFlush(toy);

    }
}
