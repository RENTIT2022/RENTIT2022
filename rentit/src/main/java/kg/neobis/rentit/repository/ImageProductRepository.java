package kg.neobis.rentit.repository;

import kg.neobis.rentit.entity.ImageProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageProductRepository extends JpaRepository<ImageProduct, Long> {

    List<ImageProduct> findByProductIdOrderByOrderNumberAsc(Long productId);

    ImageProduct findByProductIdAndOrderNumber(Long productId, byte orderNumber);

    @Modifying(
            clearAutomatically = true,
            flushAutomatically = true
    )
    @Query(
            value = "UPDATE image_product " +
                    "SET order_number = order_number - 1 " +
                    "WHERE order_number > :deletedOrder " +
                    "AND product_id = :productId",
            nativeQuery = true
    )
    void setOrderNumbersDown(int deletedOrder, Long productId);

    void deleteByImageIdAndProductId(Long imageId, Long productId);

}
