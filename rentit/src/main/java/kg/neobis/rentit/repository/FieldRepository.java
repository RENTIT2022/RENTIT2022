package kg.neobis.rentit.repository;

import kg.neobis.rentit.entity.Field;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FieldRepository extends JpaRepository<Field, Long> {

    Optional<Field> findByName(String name);

}
