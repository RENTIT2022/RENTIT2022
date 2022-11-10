package kg.neobis.rentit.repository;

import kg.neobis.rentit.entity.CategoryField;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryFieldRepository extends JpaRepository<CategoryField, Long> {

    List<CategoryField> findByCategoryIdOrderByFieldName(Long id);

}
