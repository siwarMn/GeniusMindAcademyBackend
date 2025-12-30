package com.pfeproject.GeniusMind.Services.impl;
import com.pfeproject.GeniusMind.Services.QuizService;
import com.pfeproject.GeniusMind.dto.QuizDto;
import com.pfeproject.GeniusMind.dto.QuizQuestionDto;
import com.pfeproject.GeniusMind.Entity.Quiz;
import com.pfeproject.GeniusMind.Entity.QuizQuestion;
import com.pfeproject.GeniusMind.Repository.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuizServiceImpl implements QuizService {

    @Autowired
    private QuizRepository quizRepository;

    @Override
    public List<QuizDto> getAllQuizzes() {
        /*return quizRepository.findAllByIsActiveTrue()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());*/

        // DONNÉES STATIQUES POUR TEST
        QuizQuestionDto q1 = new QuizQuestionDto();
        q1.setId(1);
        q1.setQuestion("Quelle est la valeur de 2x + 5 = 15 ?");
        q1.setOptions(List.of("x = 5", "x = 10", "x = 7.5", "x = 8"));
        q1.setCorrectAnswer("x = 5");
        q1.setExplanation("2x + 5 = 15 ⇒ 2x = 10 ⇒ x = 5");

        QuizQuestionDto q2 = new QuizQuestionDto();
        q2.setId(2);
        q2.setQuestion("Quelle est l'aire d'un cercle de rayon 3 cm ?");
        q2.setOptions(List.of("9π cm²", "6π cm²", "3π cm²", "12π cm²"));
        q2.setCorrectAnswer("9π cm²");
        q2.setExplanation("Aire = π × r² = π × 3² = 9π");

        QuizDto quiz = new QuizDto();
        quiz.setId(1);
        quiz.setTitle("Quiz de Mathématiques Avancées");
        quiz.setDescription("Testez vos connaissances en algèbre et géométrie avancée");
        quiz.setLevelId(3);
        quiz.setIsActive(true);
        quiz.setQuestions(List.of(q1, q2));

        return List.of(quiz);
    }

    @Override
    public QuizDto getQuizById(Integer id) {
        Quiz quiz = quizRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Quiz non trouvé"));
        return convertToDto(quiz);
    }

    // mapping ultra-simple
    private QuizDto convertToDto(Quiz quiz) {
        QuizDto dto = new QuizDto();
        dto.setId(quiz.getId());
        dto.setTitle(quiz.getTitle());
        dto.setDescription(quiz.getDescription());
        dto.setLevelId(quiz.getLevelId());
        dto.setIsActive(quiz.getIsActive());
        dto.setQuestions(
                quiz.getQuestions()
                        .stream()
                        .map(this::convertQuestionToDto)
                        .collect(Collectors.toList())
        );
        return dto;
    }

    private QuizQuestionDto convertQuestionToDto(QuizQuestion q) {
        QuizQuestionDto qDto = new QuizQuestionDto();
        qDto.setId(q.getId());
        qDto.setQuestion(q.getQuestion());
        qDto.setOptions(q.getOptions());
        qDto.setCorrectAnswer(q.getCorrectAnswer());
        qDto.setExplanation(q.getExplanation());
        return qDto;
    }
}
