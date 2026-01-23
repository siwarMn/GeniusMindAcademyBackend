package com.pfeproject.GeniusMind.Services.impl;
import com.pfeproject.GeniusMind.Entity.ReclamationComment;
import com.pfeproject.GeniusMind.Entity.reclamation;
import com.pfeproject.GeniusMind.Exceptions.ReclamationNotFoundException;
import com.pfeproject.GeniusMind.Repository.ReclamationCommentRepository;
import com.pfeproject.GeniusMind.Repository.ReclamationRepository;
import com.pfeproject.GeniusMind.Services.ReclamationService;
import com.pfeproject.GeniusMind.dto.CommentResponse;
import com.pfeproject.GeniusMind.dto.ReclamationRequest;
import com.pfeproject.GeniusMind.dto.ReclamationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReclamationServiceImpl implements ReclamationService {

    @Autowired
    private ReclamationRepository reclamationRepository;

    @Autowired
    private ReclamationCommentRepository commentRepository;

    @Override
    public ReclamationResponse AddReclamation(ReclamationRequest request) {
        reclamation reclam = new reclamation();
        reclam.setTitre(request.getTitre());
        reclam.setCategorie(request.getCategorie());
        reclam.setDescription(request.getDescription());
        reclam.setCreerpar(request.getCreerpar());
        reclam.setStatus("Open");
        reclam.setPriority(request.getPriority() != null ? request.getPriority() : "Medium");
        reclam.setCreatedAt(LocalDateTime.now());

        reclamation saved = reclamationRepository.save(reclam);
        return convertToResponse(saved);
    }

    @Override
    public List<ReclamationResponse> GetAll() {
        List<reclamation> reclamations = reclamationRepository.findAll();
        List<ReclamationResponse> responses = new ArrayList<>();
        for (reclamation rec : reclamations) {
            responses.add(convertToResponse(rec));
        }
        return responses;
    }

    @Override
    public ReclamationResponse getReclamById(Long id) {
        reclamation reclam = findReclamationById(id);
        return convertToResponse(reclam);
    }

    @Override
    public long GetCount() {
        return reclamationRepository.count();
    }

    @Override
    public ReclamationResponse updateStatus(Long id, String status) {
        reclamation reclam = findReclamationById(id);
        reclam.setStatus(status);
        reclamation updated = reclamationRepository.save(reclam);
        return convertToResponse(updated);
    }

    @Override
    public void deleteReclamation(Long id) {
        reclamation reclam = findReclamationById(id);
        reclamationRepository.delete(reclam);
    }

    @Override
    public CommentResponse addComment(Long reclamationId, String author, String text) {
        reclamation reclam = findReclamationById(reclamationId);

        ReclamationComment comment = new ReclamationComment();
        comment.setAuthor(author);
        comment.setText(text);
        comment.setDate(LocalDateTime.now());
        comment.setReclamation(reclam);

        ReclamationComment saved = commentRepository.save(comment);
        return convertCommentToResponse(saved);
    }

    private reclamation findReclamationById(Long id) {
        Optional<reclamation> optionalReclamation = reclamationRepository.findById(id);
        return optionalReclamation.orElseThrow(() ->
            new ReclamationNotFoundException("Réclamation non trouvée pour l'ID : " + id));
    }

    @Override
    public ReclamationResponse assignReclamation(Long id, String assignedTo) {
        reclamation reclam = findReclamationById(id);
        reclam.setAssignedTo(assignedTo);
        reclamation updated = reclamationRepository.save(reclam);
        return convertToResponse(updated);
    }

    @Override
    public ReclamationResponse rateReclamation(Long id, Integer rating) {
        reclamation reclam = findReclamationById(id);
        reclam.setRating(rating);
        reclamation updated = reclamationRepository.save(reclam);
        return convertToResponse(updated);
    }

    private ReclamationResponse convertToResponse(reclamation reclam) {
        ReclamationResponse response = new ReclamationResponse();
        response.setId(reclam.getId());
        response.setTitre(reclam.getTitre());
        response.setCategorie(reclam.getCategorie());
        response.setDescription(reclam.getDescription());
        response.setCreerpar(reclam.getCreerpar());
        response.setStatus(reclam.getStatus());
        response.setPriority(reclam.getPriority());
        response.setAssignedTo(reclam.getAssignedTo());
        response.setRating(reclam.getRating());
        response.setCreatedAt(reclam.getCreatedAt());

        List<CommentResponse> commentResponses = new ArrayList<>();
        if (reclam.getComments() != null) {
            for (ReclamationComment comment : reclam.getComments()) {
                commentResponses.add(convertCommentToResponse(comment));
            }
        }
        response.setComments(commentResponses);

        return response;
    }

    private CommentResponse convertCommentToResponse(ReclamationComment comment) {
        CommentResponse response = new CommentResponse();
        response.setId(comment.getId());
        response.setAuthor(comment.getAuthor());
        response.setText(comment.getText());
        response.setDate(comment.getDate());
        return response;
    }
}
