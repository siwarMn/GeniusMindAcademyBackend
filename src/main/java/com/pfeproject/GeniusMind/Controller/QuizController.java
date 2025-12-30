package com.pfeproject.GeniusMind.Controller;
import com.pfeproject.GeniusMind.dto.QuizDto;
import com.pfeproject.GeniusMind.Services.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/quiz")
@RequiredArgsConstructor
@CrossOrigin(origins = "*") // adapter au besoin
public class QuizController {

    private final QuizService service;

    @GetMapping
    public List<QuizDto> getAllQuizzes() {
        return service.getAllQuizzes();
    }

    @GetMapping("/{id}")
    public QuizDto getQuizById(@PathVariable Integer id) {
        return service.getQuizById(id);
    }
}