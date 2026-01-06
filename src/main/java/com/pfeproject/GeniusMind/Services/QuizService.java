package com.pfeproject.GeniusMind.Services;
import com.pfeproject.GeniusMind.dto.QuizDto;
import com.pfeproject.GeniusMind.dto.QuizScoreDto;

import java.util.List;

public interface QuizService {

    List<QuizDto> getStudentQuizzes(String studentId);

    QuizDto getQuizDetails(Long quizId);

    QuizScoreDto startQuiz(String studentId, Long quizId);

    QuizScoreDto submitQuizScore(String studentId, Long quizId, Integer score, Integer total, Long duration);

    List<QuizScoreDto> getStudentScores(Long studentId);

    QuizScoreDto getQuizResult(String student, Long quizId) ;

    }
