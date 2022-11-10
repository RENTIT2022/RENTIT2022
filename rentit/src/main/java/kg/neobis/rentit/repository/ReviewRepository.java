package kg.neobis.rentit.repository;

import kg.neobis.rentit.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    Review findByUserIdAndProductId(Long userId, Long productId);

}
