package com.pfeproject.GeniusMind.Controller;

import com.pfeproject.GeniusMind.Entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {

    private String token;
    private Role role;
    private String Nom;
    private String prenom;
}
