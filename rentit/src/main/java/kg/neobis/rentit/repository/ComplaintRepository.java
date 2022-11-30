package kg.neobis.rentit.repository;

import kg.neobis.rentit.entity.Complaint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComplaintRepository extends JpaRepository<Complaint, Long> {

    @Query(
            value = "SELECT COUNT(id) FROM Complaint " +
                    "WHERE addressee_id = :addresseeId",
            nativeQuery = true
    )
    Integer getComplaintsNumberByAddresseeId(Long addresseeId);

    @Query(
            value = "SELECT * FROM complaint c " +
                    "WHERE c.sender_id = :senderId " +
                    "ORDER BY c.local_date_time DESC " +
                    "LIMIT 20 OFFSET :offset",
            nativeQuery = true
    )
    List<Complaint> getComplaintsSentFromUser(Long senderId, int offset);

    @Query(
            value = "SELECT * FROM complaint c " +
                    "WHERE c.addressee_id = :addresseeId " +
                    "ORDER BY c.local_date_time DESC " +
                    "LIMIT 20 OFFSET :offset",
            nativeQuery = true
    )
    List<Complaint> getComplaintsSentToUser(Long addresseeId, int offset);

}
