package kg.neobis.rentit.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import kg.neobis.rentit.dto.QuestionDto;
import kg.neobis.rentit.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/questions")
@Validated
@RequiredArgsConstructor
@Tag(name = "Question Controller", description = "The Question API with documentation annotations")
public class QuestionController {

    private final QuestionService questionService;


    @GetMapping("/get/all")
    public ResponseEntity<List<QuestionDto>> getAll() {
        return ResponseEntity.ok(questionService.getAll());
    }

    @PostMapping("/create")
    public ResponseEntity<String> createQuestion(QuestionDto dto) {
        return ResponseEntity.ok(questionService.createQuestion(dto));
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateQuestion(QuestionDto dto) {
        return ResponseEntity.ok(questionService.updateQuestion(dto));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteQuestion(Long questionId) {
        return ResponseEntity.ok(questionService.deleteQuestion(questionId));
    }

}
