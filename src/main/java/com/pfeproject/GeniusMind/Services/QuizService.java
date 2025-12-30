package com.pfeproject.GeniusMind.Services;

import com.pfeproject.GeniusMind.dto.QuizDto;

import java.util.List;

public interface QuizService {

    List<QuizDto> getAllQuizzes();
    QuizDto getQuizById(Integer id);

}
