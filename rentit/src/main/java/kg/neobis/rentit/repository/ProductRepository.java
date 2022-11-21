package kg.neobis.rentit.repository;

import kg.neobis.rentit.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query(
            value = "SELECT * FROM Product p " +
                    "ORDER BY p.creation_time DESC " +
                    "LIMIT 20 OFFSET :offset",
            nativeQuery = true
    )
    List<Product> getMainPageProducts(int offset);

    @Query(
            value = "SELECT * FROM Product p " +
                    "WHERE p.category_id = :categoryId " +
                    "ORDER BY p.creation_time DESC " +
                    "LIMIT 20 OFFSET :offset",
            nativeQuery = true
    )
    List<Product> getMainPageProductsByCategory(int offset, Long categoryId);

    @Query(
            value = "SELECT * FROM Product " +
                    "WHERE category_id IN :categories " +
                    "ORDER BY RANDOM() " +
                    "LIMIT 9",
            nativeQuery = true
    )
    List<Product> getRandomProductsByCategory(List<Long> categories);

    @Query(
            value = "SELECT * FROM Product " +
                    "ORDER BY RANDOM() LIMIT 9",
            nativeQuery = true
    )
    List<Product> getRandomProducts();

    @Query(
            "SELECT p FROM Product p WHERE LOWER(p.title) LIKE %:text% ORDER BY p.title"
    )
    List<Product> getProductBySearch(String text);

}
