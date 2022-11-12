package kg.neobis.rentit.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import kg.neobis.rentit.dto.ProductDetailsDto;
import kg.neobis.rentit.dto.ProductPageDto;
import kg.neobis.rentit.dto.ProductRegistrationDto;
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
    public ResponseEntity<ProductRegistrationDto> publishProductDetails(@RequestBody ProductRegistrationDto dto) {
        return ResponseEntity.ok(productService.publishProductDetails(dto));
    }

    @PostMapping("/publish/image/{productId}/{order}")
    public ResponseEntity<String> publishProductImage(@RequestPart MultipartFile file,
                                                      @PathVariable Long productId,
                                                      @PathVariable byte order) {
        return ResponseEntity.ok(productService.publishProductImage(file, productId, order));
    }

    @PutMapping("/update/details/{productId}")
    public ResponseEntity<ProductRegistrationDto> updateProductDetails(@PathVariable Long productId,
                                                                @RequestBody ProductRegistrationDto dto) {
        return ResponseEntity.ok(productService.updateProductDetails(productId, dto));
    }

    @PutMapping("/update/image-replace/{imageId}")
    public ResponseEntity<String> updateProductReplaceImage(@RequestPart MultipartFile file,
                                                            @PathVariable Long imageId) {
        return ResponseEntity.ok(productService.updateProductReplaceImage(file, imageId));
    }

    @PutMapping("/activate/{productId}")
    public ResponseEntity<String> activateProduct(@PathVariable Long productId) {
        return ResponseEntity.ok(productService.activateProduct(productId));
    }

    @PutMapping("/deactivate/{productId}")
    public ResponseEntity<String> deactivateProduct(@PathVariable Long productId) {
        return ResponseEntity.ok(productService.deactivateProduct(productId));
    }

    @PostMapping("/add-to-favorites/{productId}")
    public ResponseEntity<String> addProductToFavorites(@PathVariable Long productId) {
        return ResponseEntity.ok(productService.addProductToFavorites(productId));
    }

    @DeleteMapping("/delete-from-favorites/{productId}")
    public ResponseEntity<String> deleteProductFromFavorites(@PathVariable Long productId) {
        return ResponseEntity.ok(productService.deleteProductFromFavorites(productId));
    }

    @GetMapping("/get/user-products")
    public ResponseEntity<List<ProductPageDto>> getUserProducts() {
        return ResponseEntity.ok(productService.getUserProducts());
    }

    @GetMapping("/get/search/{text}")
    public ResponseEntity<List<ProductPageDto>> getProductBySearch(@PathVariable String text) {
        return ResponseEntity.ok(productService.getProductBySearch(text));
    }

    @GetMapping("/get/main-page/{callNumber}")
    public ResponseEntity<List<ProductPageDto>> getMainPageProducts(@PathVariable int callNumber) {
        return ResponseEntity.ok(productService.getMainPageProducts(callNumber));
    }

    @GetMapping("/get/main-page-by-category/{callNumber}/{categoryId}")
    public ResponseEntity<List<ProductPageDto>> getMainPageProductsByCategory(@PathVariable int callNumber,
                                                                              @PathVariable Long categoryId) {
        return ResponseEntity.ok(productService.getMainPageProductsByCategory(callNumber, categoryId));
    }

    @GetMapping("/get/details/{productId}")
    public ResponseEntity<ProductDetailsDto> getProductDetails(@PathVariable Long productId) {
        return ResponseEntity.ok(productService.getProductDetails(productId));
    }

    @GetMapping("/get/recommendations")
    public ResponseEntity<List<ProductPageDto>> getRecommendations() {
        return ResponseEntity.ok(productService.getRecommendations());
    }

    @GetMapping("/get/favorites")
    public ResponseEntity<List<ProductPageDto>> getFavorites() {
        return ResponseEntity.ok(productService.getFavorites());
    }

}
