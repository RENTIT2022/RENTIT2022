package kg.neobis.rentit.repository;

import kg.neobis.rentit.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query(
            value = "SELECT * FROM product p " +
                    "INNER JOIN \"user\" u " +
                    "ON u.id = p.user_id " +
                    "WHERE u.blocked = false " +
                    "ORDER BY p.creation_time DESC " +
                    "LIMIT 20 OFFSET :offset",
            nativeQuery = true
    )
    List<Product> getMainPageProducts(int offset);

    @Query(
            value = "SELECT * FROM Product p " +
                    "INNER JOIN \"user\" u " +
                    "ON p.user_id = u.id " +
                    "WHERE p.category_id = :categoryId " +
                    "AND u.blocked = false " +
                    "ORDER BY p.creation_time DESC " +
                    "LIMIT 20 OFFSET :offset",
            nativeQuery = true
    )
    List<Product> getMainPageProductsByCategory(int offset, Long categoryId);

    @Query(
            value = "SELECT * FROM Product " +
                    "INNER JOIN \"user\" u " +
                    "ON p.user_id = u.id " +
                    "WHERE category_id IN :categories " +
                    "AND u.blocked = false " +
                    "ORDER BY RANDOM() " +
                    "LIMIT 9",
            nativeQuery = true
    )
    List<Product> getRandomProductsByCategory(List<Long> categories);

    @Query(
            value = "SELECT * FROM Product " +
                    "INNER JOIN \"user\" u " +
                    "ON p.user_id = u.id " +
                    "WHERE u.blocked = false " +
                    "ORDER BY RANDOM() LIMIT 9",
            nativeQuery = true
    )
    List<Product> getRandomProducts();

    @Query(
            value = "SELECT p FROM Product p " +
                    "WHERE p.title LIKE %:text% " +
                    "AND p.user.blocked = false " +
                    "ORDER BY p.title"
    )
    List<Product> getProductBySearch(String text);

}
