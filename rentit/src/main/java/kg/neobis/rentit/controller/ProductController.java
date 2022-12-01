package kg.neobis.rentit.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kg.neobis.rentit.dto.*;
import kg.neobis.rentit.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/products")
@Validated
@RequiredArgsConstructor
@Tag(name = "Product Controller", description = "The Product API with documentation annotations")
public class ProductController {

    private final ProductService productService;


    @PostMapping("/publish/details")
    @Operation(summary = "Публикация деталей продукта")
    public ResponseEntity<ProductRegistrationDto> publishProductDetails(@RequestBody ProductRegistrationDto dto) {
        return ResponseEntity.ok(productService.publishProductDetails(dto));
    }

    @PostMapping("/publish/image/{productId}/{order}")
    @Operation(summary = "Публикация картинок продукта по ID продукта и очереди картинки")
    public ResponseEntity<String> publishProductImage(@RequestPart MultipartFile file,
                                                      @PathVariable Long productId,
                                                      @PathVariable byte order) {
        return ResponseEntity.ok(productService.publishProductImage(file, productId, order));
    }

    @PutMapping("/update/details/{productId}")
    @Operation(summary = "Изменение деталей продукта по ID продукта")
    public ResponseEntity<ProductRegistrationDto> updateProductDetails(@PathVariable Long productId,
                                                                @RequestBody ProductRegistrationDto dto) {
        return ResponseEntity.ok(productService.updateProductDetails(productId, dto));
    }

    @PutMapping("/update/image-replace/{imageId}")
    @Operation(summary = "Замена картинки по ID картинки")
    public ResponseEntity<String> updateProductReplaceImage(@RequestPart MultipartFile file,
                                                            @PathVariable Long imageId) {
        return ResponseEntity.ok(productService.updateProductReplaceImage(file, imageId));
    }

    @PutMapping("/update/image-delete/{imageId}/{productId}/{orderNumber}")
    @Operation(summary = "Удаление картинки по ID картинки и номера очереди")
    public ResponseEntity<String> deleteProductImage(@PathVariable Long imageId,
                                                     @PathVariable Long productId,
                                                     @PathVariable int orderNumber) {
        return ResponseEntity.ok(productService.deleteProductImage(imageId, productId, orderNumber));
    }

    @PutMapping("/activate/{productId}")
    @Operation(summary = "Активирование продукта")
    public ResponseEntity<String> activateProduct(@PathVariable Long productId) {
        return ResponseEntity.ok(productService.activateProduct(productId));
    }

    @PutMapping("/deactivate/{productId}")
    @Operation(summary = "Деактивирование продукта")
    public ResponseEntity<String> deactivateProduct(@PathVariable Long productId) {
        return ResponseEntity.ok(productService.deactivateProduct(productId));
    }

    @PostMapping("/add-to-favorites/{productId}")
    @Operation(summary = "Добавление продукта в избранные по ID продукта")
    public ResponseEntity<String> addProductToFavorites(@PathVariable Long productId) {
        return ResponseEntity.ok(productService.addProductToFavorites(productId));
    }

    @DeleteMapping("/delete-from-favorites/{productId}")
    @Operation(summary = "Добавление продукта из избранных по ID продукта")
    public ResponseEntity<String> deleteProductFromFavorites(@PathVariable Long productId) {
        return ResponseEntity.ok(productService.deleteProductFromFavorites(productId));
    }

    @GetMapping("/get/user-products")
    @Operation(summary = "Получить публикации продуктов пользователя")
    public ResponseEntity<List<ProductPageDto>> getUserProducts() {
        return ResponseEntity.ok(productService.getUserProducts());
    }

    @GetMapping("/get/search/{text}")
    @Operation(summary = "Найти продукт по тексту")
    public ResponseEntity<List<ProductPageDto>> getProductBySearch(@PathVariable String text) {
        return ResponseEntity.ok(productService.getProductBySearch(text));
    }

    @GetMapping("/get/main-page/{callNumber}")
    @Operation(summary = "Получить продукта для главной страницы")
    public ResponseEntity<List<ProductPageDto>> getMainPageProducts(@PathVariable int callNumber) {
        return ResponseEntity.ok(productService.getMainPageProducts(callNumber));
    }

    @GetMapping("/get/main-page-by-category/{callNumber}/{categoryId}")
    @Operation(summary = "Получить продукта для главной страницы по категории")
    public ResponseEntity<List<ProductPageDto>> getMainPageProductsByCategory(@PathVariable int callNumber,
                                                                              @PathVariable Long categoryId) {
        return ResponseEntity.ok(productService.getMainPageProductsByCategory(callNumber, categoryId));
    }

    @GetMapping("/get/details/{productId}")
    @Operation(summary = "Получить детали определенного продукта по айди продукта")
    public ResponseEntity<ProductDetailsDto> getProductDetails(@PathVariable Long productId) {
        return ResponseEntity.ok(productService.getProductDetails(productId));
    }

    @GetMapping("/get/reviews/{productId}")
    @Operation(summary = "Получить отзывы определенного продукта по айди продукта")
    public ResponseEntity<List<ProductReviewDto>> getProductReviews(@PathVariable Long productId) {
        return ResponseEntity.ok(productService.getProductReviews(productId));
    }

    @GetMapping("/get/recommendations")
    @Operation(summary = "Получить рекоммендации пользователя")
    public ResponseEntity<List<ProductPageDto>> getRecommendations() {
        return ResponseEntity.ok(productService.getRecommendations());
    }

    @GetMapping("/get/favorites")
    @Operation(summary = "Получить избранные продукты")
    public ResponseEntity<List<ProductPageDto>> getFavorites() {
        return ResponseEntity.ok(productService.getFavorites());
    }

    @GetMapping("/get/map-products-by-category/{categoryId}")
    @Operation(summary = "Получить продукты для карты по категории")
    public ResponseEntity<List<ProductMapDto>> getMapProducts(@PathVariable Long categoryId) {
        return ResponseEntity.ok(productService.getMapProducts(categoryId));
    }

    @GetMapping("/get/similar-by-category/{categoryId}")
    @Operation(summary = "Получить схожие продукты по категории")
    public ResponseEntity<List<ProductPageDto>> getSimilarProducts(@PathVariable Long categoryId) {
        return ResponseEntity.ok(productService.getSimilarProducts(categoryId));
    }

    @DeleteMapping("/delete/{productId}")
    @Operation(summary = "Удалить продукт по айди продукта")
    public ResponseEntity<String> deleteProduct(@PathVariable Long productId) {
        return ResponseEntity.ok(productService.deleteProduct(productId));
    }

}
