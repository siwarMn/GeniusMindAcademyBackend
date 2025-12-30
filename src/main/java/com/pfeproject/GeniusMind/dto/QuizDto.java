package com.pfeproject.GeniusMind.dto;
import lombok.Data;
import java.util.List;

@Data
public class QuizDto {
    private Integer id;
    private String title;
    private String description;
    private Integer levelId;
    private Boolean isActive;
    private List<QuizQuestionDto> questions;
}