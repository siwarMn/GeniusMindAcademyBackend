package com.pfeproject.GeniusMind.dto;

import lombok.Data;

@Data
public class VoteDTO {
    private Boolean isUpvote; // true = upvote, false = downvote
    private String userId; // ID de l'utilisateur qui vote
}