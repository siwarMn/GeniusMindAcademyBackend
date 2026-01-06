package com.pfeproject.GeniusMind.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuizScoreDto {
    private Long id;
    private Long studentId; // Id de l'étudiant
    private Long quizId;    // Id du quiz
    private Integer score;
    private Integer total;
    private Long durationSec;
    private Boolean finished;
}
