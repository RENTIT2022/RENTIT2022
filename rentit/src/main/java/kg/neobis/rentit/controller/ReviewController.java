package kg.neobis.rentit.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kg.neobis.rentit.dto.AddReviewDto;
import kg.neobis.rentit.dto.UpdateReviewDto;
import kg.neobis.rentit.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reviews")
@Validated
@RequiredArgsConstructor
@Tag(name = "Review Controller", description = "The Review API with documentation annotations")
public class ReviewController {

    private final ReviewService reviewService;


    @PostMapping("/add")
    @Operation(summary = "Добавить отзыв")
    public ResponseEntity<String> addProductReview(@RequestBody AddReviewDto dto) {
        return ResponseEntity.ok(reviewService.addProductReview(dto));
    }

    @PutMapping("/update")
    @Operation(summary = "Изменить отзыв")
    public ResponseEntity<String> updateProductReview(@RequestBody UpdateReviewDto dto) {
        return ResponseEntity.ok(reviewService.updateProductReview(dto));
    }

    @DeleteMapping("/delete/{reviewId}")
    @Operation(summary = "Удалить отзыв")
    public ResponseEntity<String> deleteProductReview(@PathVariable Long reviewId) {
        return ResponseEntity.ok(reviewService.deleteProductReview(reviewId));
    }
}
