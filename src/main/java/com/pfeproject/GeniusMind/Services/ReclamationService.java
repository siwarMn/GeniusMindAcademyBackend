package com.pfeproject.GeniusMind.Services;

import com.pfeproject.GeniusMind.dto.CommentResponse;
import com.pfeproject.GeniusMind.dto.ReclamationRequest;
import com.pfeproject.GeniusMind.dto.ReclamationResponse;

import java.util.List;

public interface ReclamationService {

    ReclamationResponse AddReclamation(ReclamationRequest request);

    List<ReclamationResponse> GetAll();

    ReclamationResponse getReclamById(Long id);

    long GetCount();

    ReclamationResponse updateStatus(Long id, String status);

    void deleteReclamation(Long id);

    CommentResponse addComment(Long reclamationId, String author, String text);

    ReclamationResponse assignReclamation(Long id, String assignedTo);

    ReclamationResponse rateReclamation(Long id, Integer rating);
}
