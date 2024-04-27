package com.pfeproject.GeniusMind.Controller;

import com.pfeproject.GeniusMind.Entity.Question;
import com.pfeproject.GeniusMind.Services.QuestionService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/auth")
@AllArgsConstructor
public class QuestionController {
    @Autowired
    private QuestionService questionService;

    @PostMapping("/addQuestion/{idLevel}")
    Question addQuestion(@RequestBody Question questionDto, @PathVariable("idLevel") Long idLevel) {
        System.out.println(" question vené du service heeeeeeeeeeee " + questionDto);
        return questionService.addQuestion(questionDto, idLevel);
    }

    @GetMapping("/getQuestions/{idLevel}")
    List<Question> getQuestions(@PathVariable("idLevel") Long idLevel)  {
        return questionService.getQuestions(idLevel);
    }
}
