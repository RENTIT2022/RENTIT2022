package kg.neobis.rentit.repository;

import kg.neobis.rentit.entity.PaidAdWaitingList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaidAdWaitingListRepository extends JpaRepository<PaidAdWaitingList, Long> {
}
