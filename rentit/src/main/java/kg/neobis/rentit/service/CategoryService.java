package kg.neobis.rentit.service;

import kg.neobis.rentit.dto.CategoryFieldsDto;
import kg.neobis.rentit.dto.CategoryNameDto;
import kg.neobis.rentit.entity.Category;
import kg.neobis.rentit.entity.CategoryField;
import kg.neobis.rentit.exception.ResourceNotFoundException;
import kg.neobis.rentit.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;


    public List<CategoryNameDto> getCategoryNames() {
        return categoryRepository.findAll().stream()
                .map(
                        entity ->
                                new CategoryNameDto(entity.getId(), entity.getName())
                )
                .collect(Collectors.toList());
    }

    public List<String> getCategoryFields(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Category was not found with ID: "
                                + categoryId)
                );

        Set<CategoryField> categoryFields = category.getFields();

        List<String> fields = new ArrayList<>();

        for (CategoryField entity : categoryFields) {
            fields.add(entity.getField().getName());
        }

        Collections.sort(fields);

        return fields;
    }

    public List<CategoryFieldsDto> getCategoryFields() {
        return categoryRepository.findAll().stream()
                .map(
                        category -> {
                            CategoryFieldsDto dto = new CategoryFieldsDto();

                            dto.setCategoryId(category.getId());
                            dto.setName(category.getName());

                            Map<String, String> fields = new TreeMap<>();

                            category.getFields().forEach(
                                    categoryField -> fields.put(categoryField.getField().getName(), "")
                            );

                            dto.setFields(fields);

                            return dto;
                        }
                )
                .collect(Collectors.toList());
    }

}
