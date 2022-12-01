package kg.neobis.rentit.repository;

import kg.neobis.rentit.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRepository extends JpaRepository<Chat, Long> {
}
