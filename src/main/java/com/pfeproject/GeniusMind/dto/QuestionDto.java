package com.pfeproject.GeniusMind.dto;

import lombok.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionDto {
    private Long id;
    private String label;
    private Long quizId; // Id du quiz associé
    private Long levelId; // Id du niveau associé
    private List<OptionDto> options; // Liste des options
}
