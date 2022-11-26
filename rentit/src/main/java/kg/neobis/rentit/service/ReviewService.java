package kg.neobis.rentit.service;

import kg.neobis.rentit.dto.AddReviewDto;
import kg.neobis.rentit.dto.UpdateReviewDto;
import kg.neobis.rentit.entity.Product;
import kg.neobis.rentit.entity.Review;
import kg.neobis.rentit.entity.User;
import kg.neobis.rentit.exception.BadRequestException;
import kg.neobis.rentit.exception.ResourceNotFoundException;
import kg.neobis.rentit.repository.ProductRepository;
import kg.neobis.rentit.repository.ReviewRepository;
import kg.neobis.rentit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final UserRepository userRepository;

    private final ProductRepository productRepository;

    private final ReviewRepository reviewRepository;


    public String addProductReview(AddReviewDto dto) {
        Product product = productRepository.findById(dto.getProductId())
                .orElseThrow(
                        () -> new ResourceNotFoundException("Product was not found with ID: "
                                + dto.getProductId())
                );

        User user = userRepository.findByEmail(UserService.getAuthentication().getName());

        if(user.getId().equals(product.getUser().getId())) {
            throw new BadRequestException("Вы не можете добавить отзыв к своему продукту, не положено.");
        }

        Review review = reviewRepository.findByUserIdAndProductId(user.getId(), product.getId());

        if(review == null) {
            review = new Review();

            review.setUser(user);
            review.setProduct(product);
        }

        review.setText(dto.getText());
        review.setStar(dto.getStar());
        review.setDateTime(LocalDateTime.now());

        reviewRepository.save(review);

        return "The review was successfully added.";
    }

    public String updateProductReview(UpdateReviewDto dto) {
        Review review = reviewRepository.findById(dto.getReviewId())
                .orElseThrow(
                        () -> new ResourceNotFoundException("Review was not found with ID: "
                                + dto.getReviewId())
                );

        User user = userRepository.findByEmail(UserService.getAuthentication().getName());

        if(!review.getUser().getId().equals(user.getId())) {
            throw new BadRequestException("Вы не можете изменить отзыв, так как он не ваш.");
        }

        review.setStar(dto.getStar());
        review.setText(dto.getText());
        review.setDateTime(LocalDateTime.now());

        reviewRepository.save(review);

        return "The review was successfully updated.";
    }

    public String deleteProductReview(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Review was not found with ID: "
                                + reviewId)
                );

        Long userId = userRepository.findByEmail(UserService.getAuthentication().getName()).getId();

        if(!review.getUser().getId().equals(userId)) {
            return "You cannot delete review since you are not the one who published the review.";
        }

        reviewRepository.deleteById(reviewId);

        return "The review was successfully deleted.";
    }

}
