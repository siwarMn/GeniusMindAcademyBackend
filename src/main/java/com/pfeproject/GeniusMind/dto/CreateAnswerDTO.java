package com.pfeproject.GeniusMind.dto;

import lombok.Data;

@Data
public class CreateAnswerDTO {
    private Long questionId;
    private String content;
    private String author; // Sera remplacé par le user authentifié
}
