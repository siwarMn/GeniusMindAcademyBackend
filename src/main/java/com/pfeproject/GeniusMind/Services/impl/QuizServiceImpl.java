package com.pfeproject.GeniusMind.Services.impl;

import com.pfeproject.GeniusMind.Entity.*;
import com.pfeproject.GeniusMind.Repository.*;
import com.pfeproject.GeniusMind.Services.QuizService;
import com.pfeproject.GeniusMind.dto.*;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuizServiceImpl implements QuizService {

    private final QuizRepository quizRepository;
    private final QuizAssignmentRepository quizAssignmentRepository;
    private final QuizScoreRepository quizScoreRepository;
    private final UserRepository userRepository;

    // =========================================================
    // QUIZ POUR ÉTUDIANT
    // =========================================================

    @Override
    public List<QuizDto> getStudentQuizzes(String studentName) {

        Long studentId = userRepository.findIdByFirstname(studentName);

        return quizAssignmentRepository.findByStudentId(studentId)
                .stream()
                .map(assignment -> {
                    Quiz quiz = assignment.getQuiz();
                    QuizDto dto = convertQuizToDto(quiz);

                    dto.setCompleted(
                            assignment.getCompleted() != null && assignment.getCompleted()
                    );

                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public QuizDto getQuizDetails(Long quizId) {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() ->
                        new RuntimeException("Quiz non trouvé avec l'ID: " + quizId)
                );

        return convertQuizToDto(quiz);
    }

    // =========================================================
    // DÉMARRER & SOUMETTRE QUIZ
    // =========================================================

    @Override
    public QuizScoreDto startQuiz(String studentName, Long quizId) {

        Long studentId = userRepository.findIdByFirstname(studentName);

        QuizAssignment assignment = quizAssignmentRepository
                .findByStudentIdAndQuizId(studentId, quizId)
                .orElseThrow(() -> new RuntimeException("Assignment non trouvé"));

        assignment.setStarted(true);
        quizAssignmentRepository.save(assignment);

        QuizScore score = quizScoreRepository
                .findByStudentIdAndQuizId(studentId, quizId)
                .orElseGet(() -> {
                    QuizScore newScore = new QuizScore();
                    newScore.setStudent(assignment.getStudent());
                    newScore.setQuiz(assignment.getQuiz());
                    newScore.setFinished(false);
                    return quizScoreRepository.save(newScore);
                });

        return convertQuizScoreToDto(score);
    }

    @Override
    public QuizScoreDto submitQuizScore(
            String studentName,
            Long quizId,
            Integer scoreValue,
            Integer total,
            Long duration
    ) {

        Long studentId = userRepository.findIdByFirstname(studentName);

        QuizScore score = quizScoreRepository
                .findByStudentIdAndQuizId(studentId, quizId)
                .orElseThrow(() -> new RuntimeException("Score non trouvé"));

        score.setScore(scoreValue);
        score.setTotal(total);
        score.setDurationSec(duration);
        score.setFinished(true);

        quizScoreRepository.save(score);

        QuizAssignment assignment = quizAssignmentRepository
                .findByStudentIdAndQuizId(studentId, quizId)
                .orElseThrow(() -> new RuntimeException("Assignment non trouvé"));

        assignment.setCompleted(true);
        quizAssignmentRepository.save(assignment);

        return convertQuizScoreToDto(score);
    }

    // =========================================================
    // RÉSULTATS & SCORES
    // =========================================================

    public QuizScoreDto getQuizResult(String studentName, Long quizId) {

        Long studentId = userRepository.findIdByFirstname(studentName);

        QuizScore result = quizScoreRepository
                .findByStudentIdAndQuizId(studentId, quizId)
                .orElseThrow(() ->
                        new RuntimeException("Résultat du quiz non trouvé")
                );

        return convertQuizScoreToDto(result);
    }

    @Override
    public List<QuizScoreDto> getStudentScores(Long studentId) {
        return quizScoreRepository.findByStudentId(studentId)
                .stream()
                .map(this::convertQuizScoreToDto)
                .collect(Collectors.toList());
    }

    // =========================================================
    // CONVERSIONS ENTITY → DTO
    // =========================================================

    private QuizDto convertQuizToDto(Quiz quiz) {
        QuizDto dto = new QuizDto();
        dto.setId(quiz.getId());
        dto.setTitle(quiz.getTitle());
        dto.setDescription(quiz.getDescription());
        dto.setLevel(quiz.getLevel());
        dto.setActive(quiz.getActive());

        dto.setQuestions(
                quiz.getQuestions() != null
                        ? quiz.getQuestions().stream()
                        .map(this::convertQuestionToDto)
                        .collect(Collectors.toList())
                        : new ArrayList<>()
        );

        return dto;
    }

    private QuestionDto convertQuestionToDto(Question question) {
        QuestionDto dto = new QuestionDto();
        dto.setId(question.getId());
        dto.setLabel(question.getLabel());
        dto.setQuizId(question.getQuiz().getId());
        dto.setLevelId(
                question.getLevel() != null ? question.getLevel().getId() : null
        );

        dto.setOptions(
                question.getOptions() != null
                        ? question.getOptions().stream()
                        .map(this::convertOptionToDto)
                        .collect(Collectors.toList())
                        : new ArrayList<>()
        );

        return dto;
    }

    private OptionDto convertOptionToDto(Option option) {
        OptionDto dto = new OptionDto();
        dto.setId(option.getId());
        dto.setLabel(option.getLabel());
        dto.setCorrect(option.getCorrect());
        dto.setQuestionId(option.getQuestion().getId());
        return dto;
    }

    private QuizScoreDto convertQuizScoreToDto(QuizScore score) {
        QuizScoreDto dto = new QuizScoreDto();
        dto.setId(score.getId());
        dto.setStudentId(score.getStudent().getId().longValue());
        dto.setQuizId(score.getQuiz().getId());
        dto.setScore(score.getScore());
        dto.setTotal(score.getTotal());
        dto.setDurationSec(score.getDurationSec());
        dto.setFinished(score.getFinished());
        return dto;
    }
}
