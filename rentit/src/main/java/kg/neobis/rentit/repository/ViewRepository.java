package kg.neobis.rentit.repository;

import kg.neobis.rentit.entity.View;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ViewRepository extends JpaRepository<View, Long> {

    View findByUserIdAndCategoryId(Long userId, Long categoryId);

    List<View> findAllByUserId(Long userId);

}
