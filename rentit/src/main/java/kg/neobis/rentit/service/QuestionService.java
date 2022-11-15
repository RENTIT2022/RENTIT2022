package kg.neobis.rentit.service;

import kg.neobis.rentit.dto.QuestionDto;
import kg.neobis.rentit.entity.Question;
import kg.neobis.rentit.exception.AlreadyExistException;
import kg.neobis.rentit.exception.ResourceNotFoundException;
import kg.neobis.rentit.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;


    public List<QuestionDto> getAll() {
        return questionRepository.findAll().stream()
                .map(question -> {
                    QuestionDto dto = new QuestionDto();

                    dto.setId(question.getId());
                    dto.setQuestion(question.getQuestion());
                    dto.setAnswer(question.getAnswer());

                    return dto;
                })
                .collect(Collectors.toList());
    }

    public String createQuestion(QuestionDto dto) {
        Question question = questionRepository.findByQuestion(dto.getQuestion());

        if(question != null) {
            throw new AlreadyExistException("Такой вопрос уже создан.");
        } else {
            question = new Question();

            question.setQuestion(dto.getQuestion());
            question.setAnswer(dto.getAnswer());

            questionRepository.save(question);

            return "Вопрос был успешно создан.";
        }
    }

    public String updateQuestion(QuestionDto dto) {
        Question question = questionRepository.findById(dto.getId())
                .orElseThrow(
                        () -> new ResourceNotFoundException("Question was not found with ID: " + dto.getId())
                );

        question.setQuestion(dto.getQuestion());
        question.setAnswer(dto.getAnswer());

        questionRepository.save(question);

        return "Вопрос был успешно изменен.";
    }

    public String deleteQuestion(Long questionId) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Question was not found with ID: " + questionId)
                );

        questionRepository.delete(question);

        return "Вопрос был успешно удален.";
    }

}
