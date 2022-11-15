package kg.neobis.rentit.repository;

import kg.neobis.rentit.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {

    Question findByQuestion(String question);

}
