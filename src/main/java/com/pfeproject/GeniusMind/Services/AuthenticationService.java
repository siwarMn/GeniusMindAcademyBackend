package com.pfeproject.GeniusMind.Services;

import com.pfeproject.GeniusMind.Controller.AuthenticationRequest;
import com.pfeproject.GeniusMind.Controller.AuthenticationResponse;
import com.pfeproject.GeniusMind.Controller.RegisterRequest;
import com.pfeproject.GeniusMind.Entity.Role;
import com.pfeproject.GeniusMind.Entity.User;
import com.pfeproject.GeniusMind.Exceptions.NotFoundException;
import com.pfeproject.GeniusMind.Exceptions.UserExistException;
import com.pfeproject.GeniusMind.Repository.UserRepository;
import com.pfeproject.GeniusMind.config.JwtService;
import com.pfeproject.GeniusMind.dto.ForgotPassword;
import com.pfeproject.GeniusMind.dto.ResetPassword;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.antlr.v4.runtime.Token;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class AuthenticationService {

    private final UserRepository repository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public void register(RegisterRequest request) throws Exception {
        System.out.println("service user ENS "+ request);
        if(repository.findUserByEmail(request.getEmail()).isPresent())
            throw new UserExistException("User exists !");
        var user = User.builder()
                .firstname((request.getFirstname()))
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .niveau(request.getNiveau())
                .build();

        repository.save(user);
        var jwtToken = jwtService.generateToken(user);
        String link = "http://localhost:4200/activated?token=" + jwtToken;

        String body = emailService.buildEmail(user.getFirstname(), link);
        emailService.sendSimpleEmail(
                user.getEmail(),
                "Please confirm your account",
                body
        );
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        User user = repository.findUserByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + request.getEmail()));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid password");
        }
        System.out.println("Helllo service user  "+user.getFirstname());
        String token = jwtService.generateToken(user);

        AuthenticationResponse res = new AuthenticationResponse();
        res.setToken(token);
        res.setRole(user.getRole());
        res.setNom(user.getFirstname());
        res.setPrenom(user.getLastname());
        log.info(res.getToken());
        return res;


    }


    public String forgotpassword(ForgotPassword email){

        log.info(email.getEmail());
        User user= repository.findUserByEmail(email.getEmail()).orElseThrow();

        String token = jwtService.generateToken(user);

        String link = "http://localhost:4200/reset-password?token=" + token;
        log.info(link);
        String body = emailService.buildEmail(user.getUsername(), link);
        emailService.sendSimpleEmail(
                user.getEmail(),
                "Password Reset",
                body
        );

        return "email sent";
    }

    public void resetpassword(ResetPassword password, String token){
        if(jwtService.isTokenExpired(token))
            throw new IllegalStateException("Expired token");
        String email = jwtService.extractUsername(token);
        User user= repository.findUserByEmail(email).orElseThrow(()-> new IllegalStateException("Token invalid"));
        log.info(user.getPassword());
        log.info(password.getPassword());

        user.setPassword(passwordEncoder.encode(password.getPassword()));
        log.info(user.getPassword());
        repository.save(user);
    }

    public List<User> getAccounts() {
        return repository.findByRole("ENSEI");
    }
}
