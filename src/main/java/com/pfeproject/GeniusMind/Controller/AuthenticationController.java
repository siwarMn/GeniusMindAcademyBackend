package com.pfeproject.GeniusMind.Controller;
import com.pfeproject.GeniusMind.Entity.Role;
import com.pfeproject.GeniusMind.Entity.User;
import com.pfeproject.GeniusMind.Exceptions.NotFoundException;
import com.pfeproject.GeniusMind.Services.AuthenticationService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/auth")
@AllArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;
    @GetMapping("/GetAccounts")
    @ResponseStatus(HttpStatus.OK)
    public List<User> GetAccountsAll()  {
        System.out.println("tay");
        return service.getAccounts();
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.OK)
    public void register(@RequestBody RegisterRequest request) throws Exception {
         service.register(request);
    }


    @PostMapping("/authenticate")
    @ResponseStatus(HttpStatus.OK)
    public AuthenticationResponse authenticate(@RequestBody AuthenticationRequest request) {
        System.out.println(request);
        return service.authenticate(request);
    }

    @GetMapping("/nbelev")
    public long getNombreElevesByRole() {
        Role role = Role.USER;
        return service.getnbByRole(role);
    }

    @GetMapping("/nbEnsei")
    public long getNombreEnseignantByRole() {
        Role role = Role.ENSEI;
        return service.getnbByRole(role);
    }

    @PutMapping("/profile")
    public String updateProfile(@RequestBody User updatedUser) {
         System.out.println(updatedUser);
            service.updateUserProfile(updatedUser);
            return "Profil mis à jour avec succès !";

    }

}
