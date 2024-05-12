package com.pfeproject.GeniusMind.Controller;

import com.pfeproject.GeniusMind.Entity.Question;
import com.pfeproject.GeniusMind.Entity.reclamation;
import com.pfeproject.GeniusMind.Services.QuestionService;
import com.pfeproject.GeniusMind.Services.ReclamationService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/auth")
@AllArgsConstructor
public class ReclamationController {
    @Autowired
    private ReclamationService reclamationService;

    @PostMapping("/addReclamation")
    reclamation addReclamation(@RequestBody reclamation reclam) {
        return reclamationService.AddReclamation(reclam);
    }

    @GetMapping("/getReclam")
    List<reclamation> getAll()  {
        return reclamationService.GetAll();
    }

    @GetMapping("/getnbReclam")
    long getnbReclam()  {
        return reclamationService.GetCount();
    }
}
