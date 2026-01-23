package com.pfeproject.GeniusMind.dto;

import lombok.Data;

import java.util.List;

@Data
public class CreateQuestionDTO {
    private String title;
    private String description;
    private List<String> tags;
    private String author;
}