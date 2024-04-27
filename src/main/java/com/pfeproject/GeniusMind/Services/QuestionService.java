package com.pfeproject.GeniusMind.Services;

import com.pfeproject.GeniusMind.Entity.Level;
import com.pfeproject.GeniusMind.Entity.Question;
import com.pfeproject.GeniusMind.Repository.LevelRepository;
import com.pfeproject.GeniusMind.Repository.QuestionRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class QuestionService {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private LevelRepository levelRepository;

    public Question addQuestion(Question question, Level level) {
        level.addQuestion(question);
        levelRepository.save(level);
        return  question;
    }
    public Question addQuestion(Question questionDto, Long idLevel) {
        Level level = levelRepository.getReferenceById(idLevel);
        return addQuestion(questionDto, level);
    }

    public List<Question> getQuestions(Long idLevel) {
        Level level = levelRepository.findById(idLevel).get();

        return level.getQuestions();
    }


}

