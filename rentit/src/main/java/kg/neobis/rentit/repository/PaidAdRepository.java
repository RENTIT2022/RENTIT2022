package kg.neobis.rentit.repository;

import kg.neobis.rentit.entity.PaidAd;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaidAdRepository extends JpaRepository<PaidAd, Long> {
}
