package kg.neobis.rentit.repository;

import kg.neobis.rentit.entity.FieldProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FieldProductRepository extends JpaRepository<FieldProduct, Long> {
}
