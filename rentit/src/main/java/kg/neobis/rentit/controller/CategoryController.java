package kg.neobis.rentit.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import kg.neobis.rentit.dto.CategoryFieldsDto;
import kg.neobis.rentit.dto.CategoryNameDto;
import kg.neobis.rentit.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/categories")
@Validated
@RequiredArgsConstructor
@Tag(name = "Category Controller", description = "The Category API with documentation annotations")
public class CategoryController {

    private final CategoryService categoryService;


    @GetMapping("/get/category-fields")
    public ResponseEntity<List<CategoryFieldsDto>> getCategoryFields() {
        return ResponseEntity.ok(categoryService.getCategoryFields());
    }

    @GetMapping("/get/name-id")
    public ResponseEntity<List<CategoryNameDto>> getCategoryNames() {
        return ResponseEntity.ok(categoryService.getCategoryNames());
    }

    @GetMapping("/get/fields-by-id/{categoryId}")
    public ResponseEntity<List<String>> getCategoryFields(@PathVariable Long categoryId) {
        return ResponseEntity.ok(categoryService.getCategoryFields(categoryId));
    }

}
