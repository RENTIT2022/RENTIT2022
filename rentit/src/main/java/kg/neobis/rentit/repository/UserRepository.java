package kg.neobis.rentit.repository;

import kg.neobis.rentit.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Boolean existsByEmail(String email);

    Boolean existsByPhoneNumber(String phoneNumber);

    Boolean existsByPassportDataTin(String tin);

    User findByEmail(String email);

    Optional<User> findUserByEmail(String email);

    User findUserByPhoneNumber(String phoneNumber);

    Optional<User> findUserByResetPasswordCode(Integer code);

    @Query("SELECT u FROM User u WHERE u.isRegistrationComplete = true and u.isVerifiedByTechSupport = false")
    List<User> findAllNotVerifiedUsers();
}
