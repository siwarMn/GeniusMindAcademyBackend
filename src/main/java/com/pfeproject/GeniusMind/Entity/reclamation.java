package com.pfeproject.GeniusMind.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class reclamation {

    @Id
    @GeneratedValue(strategy  = GenerationType.IDENTITY)
    private Long id;

    private String Titre;
    private String Categorie;
    private String Description;
    private String Creerpar;
    private String status;
    private String priority;
    private String assignedTo;
    private Integer rating;
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "reclamation", cascade = CascadeType.ALL)
    private List<ReclamationComment> comments = new ArrayList<>();

}
