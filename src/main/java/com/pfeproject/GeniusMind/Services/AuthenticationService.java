package com.pfeproject.GeniusMind.Services;

import com.pfeproject.GeniusMind.Controller.AuthenticationRequest;
import com.pfeproject.GeniusMind.Controller.AuthenticationResponse;
import com.pfeproject.GeniusMind.Controller.RegisterRequest;
import com.pfeproject.GeniusMind.Entity.Role;
import com.pfeproject.GeniusMind.Entity.User;
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
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.Base64;
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
    public void register(MultipartFile file, RegisterRequest request) throws Exception {
        if(repository.findUserByEmail(request.getEmail()).isPresent())
            throw new UserExistException("User exists !");
        if (file.getOriginalFilename() != null )














        {
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        }
        var user = User.builder()
                .firstname((request.getFirstname()))
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .niveau(request.getNiveau())
                .image(Base64.getEncoder().encodeToString(file.getBytes()))
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
        String token = jwtService.generateToken(user);

        AuthenticationResponse res = new AuthenticationResponse();
        res.setToken(token);
        res.setRole(user.getRole());
        res.setNom(user.getFirstname());
        res.setPrenom(user.getLastname());
        res.setImage(user.getImage());
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
        Role role = Role.ENSEI;
        return repository.findUsersByRole(role);

    }

    public long getnbByRole(Role role) {
        return repository.countByRole(role);
    }

    public void updateUserProfile(User userDetails) {
        System.out.println("dkhlna l service "+userDetails);
        User existingUser = repository.findByEmail(userDetails.getEmail());
        System.out.println("existing user "+existingUser);
        if (existingUser != null) {
            if(userDetails.getFirstname() != null )
            {
                existingUser.setFirstname(userDetails.getFirstname());
            }

            if(userDetails.getLastname() != null )
            {
                existingUser.setLastname(userDetails.getLastname());
            }
            if(userDetails.getEmail() != null )
            {
                existingUser.setEmail(userDetails.getEmail());
            }
            if(userDetails.getPassword() != null )
            {
                existingUser.setPassword(userDetails.getPassword());
            }
            if(userDetails.getNiveau() != null )
            {
                existingUser.setNiveau(userDetails.getNiveau());
            }
            repository.save(existingUser);
        }
    }

    public void deleteProfile(Integer idprofile) {
        repository.deleteById(idprofile);
    }
}