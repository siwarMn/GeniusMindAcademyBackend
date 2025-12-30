package com.pfeproject.GeniusMind.dto;
import lombok.Data;
import java.util.List;

@Data
public class QuizQuestionDto {
    private Integer id;
    private String question;
    private List<String> options;
    private String correctAnswer;
    private String explanation;
}