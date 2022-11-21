package kg.neobis.rentit.repository;

import kg.neobis.rentit.entity.ImageUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageUserRepository extends JpaRepository<ImageUser, Long> {
}
