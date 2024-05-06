package com.pfeproject.GeniusMind.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

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

}
