package com.pfeproject.GeniusMind.Controller;

import com.pfeproject.GeniusMind.dto.*;
import com.pfeproject.GeniusMind.Services.ReclamationService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/auth")
@AllArgsConstructor
public class ReclamationController {
    @Autowired
    private ReclamationService reclamationService;

    @PostMapping("/addReclamation")
    ReclamationResponse addReclamation(@RequestBody ReclamationRequest request) {
        return reclamationService.AddReclamation(request);
    }

    @GetMapping("/getReclam")
    List<ReclamationResponse> getAll()  {
        return reclamationService.GetAll();
    }

    @GetMapping("/getReclambyId/{id}")
    ReclamationResponse getreclamById(@PathVariable Long id)  {
        return reclamationService.getReclamById(id);
    }

    @GetMapping("/getnbReclam")
    long getnbReclam()  {
        return reclamationService.GetCount();
    }

    @PutMapping("/updateStatus/{id}")
    ReclamationResponse updateStatus(@PathVariable Long id, @RequestBody StatusUpdateRequest request) {
        return reclamationService.updateStatus(id, request.getStatus());
    }

    @DeleteMapping("/deleteReclam/{id}")
    ResponseEntity<String> deleteReclamation(@PathVariable Long id) {
        reclamationService.deleteReclamation(id);
        return ResponseEntity.ok("Deleted");
    }

    @PostMapping("/addComment/{id}")
    CommentResponse addComment(@PathVariable Long id, @RequestBody CommentRequest request) {
        return reclamationService.addComment(id, request.getAuthor(), request.getText());
    }

    @PutMapping("/assignReclam/{id}")
    ReclamationResponse assignReclamation(@PathVariable Long id, @RequestBody AssignmentRequest request) {
        return reclamationService.assignReclamation(id, request.getAssignedTo());
    }

    @PutMapping("/rateReclam/{id}")
    ReclamationResponse rateReclamation(@PathVariable Long id, @RequestBody RatingRequest request) {
        return reclamationService.rateReclamation(id, request.getRating());
    }
}
