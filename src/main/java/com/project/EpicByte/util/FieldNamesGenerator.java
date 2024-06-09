package com.project.EpicByte.util;

import com.project.EpicByte.model.entity.BaseProduct;
import com.project.EpicByte.model.entity.enums.LanguageEnum;
import com.project.EpicByte.model.entity.enums.MovieCarrierEnum;
import com.project.EpicByte.model.entity.enums.MusicCarrierEnum;
import com.project.EpicByte.model.entity.enums.ProductTypeEnum;
import com.project.EpicByte.model.entity.productEntities.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * A utility class that extracts several values for each of the products in the store, to help utilize a single html
 * page for adding all product types.

 *  1. "fieldName", which is the name defined in the class and its corresponding DTO, usually in camelCase. This is used
 *      to set our thymeleaf "th:field="${fieldName}"" fields.

 *  2. "prettyFieldName" is the name we use to display in the webpage, which would look like "Pretty field name". It
 *      extracts the fieldName camelCase and converts to capitalized pretty text, we can use for our "<label>" fields.

 *  3. "fieldType", is the type of field we have - String, Integer, LocalDate, etc. to determine how what type of
 *      field we would need in Thymeleaf. For example - fieldType: String, would need <input type="text">,
 *      fieldType: LocalDate, would need <input type="date">, and so on.
 */

@Getter @Setter @NoArgsConstructor
public class FieldNamesGenerator {

    @Getter @AllArgsConstructor
    public static class FieldEntity {
        String fieldName;
        String prettyFieldName;
        String fieldType;
    }

    protected List<FieldEntity> getFieldNames(String type) {
        List<FieldEntity> fields = new ArrayList<>();

        for (Field field : BaseProduct.class.getDeclaredFields()) {
            if (field.getType() == ProductTypeEnum.class) {
                continue;
            }

            fields.add(new FieldEntity(
                    field.getName(),
                    getPrettyName(field.getName()),
                    field.getType().getSimpleName()
            ));
        }

        Field[] fieldArray = switch (type) {
            case "book" -> Book.class.getDeclaredFields();
            case "textbook" -> Textbook.class.getDeclaredFields();
            case "music" -> Music.class.getDeclaredFields();
            case "movie" -> Movie.class.getDeclaredFields();
            case "toy" -> Toy.class.getDeclaredFields();
            default -> null;
        };

        assert fieldArray != null;
        for (Field field : fieldArray) {
            String fieldType;

            if (field.getType() == ProductTypeEnum.class || field.getType() == LanguageEnum.class ||
            field.getType() == MovieCarrierEnum.class || field.getType() == MusicCarrierEnum.class) {
                fieldType = "Enum";
            } else {
                fieldType = field.getType().getSimpleName();
            }

            fields.add(new FieldEntity(
                    field.getName(),
                    getPrettyName(field.getName()),
                    fieldType));
        }

        return fields;
    }

    private String getPrettyName(String variableName) {
        String withSpaces = variableName.replaceAll("([A-Z])", " $1").trim();
        return convertToSentenceCase(withSpaces);
    }

    public static String convertToSentenceCase(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }

        StringBuilder result = new StringBuilder(text.length());
        String[] words = text.split("\\s", 2);
        result.append(Character.toUpperCase(words[0].charAt(0)));
        if (words[0].length() > 1) {
            result.append(words[0].substring(1).toLowerCase());
        }
        if(words.length > 1) {
            result.append(" ")
                    .append(words[1].toLowerCase());
        }

        return result.toString();
    }
}
