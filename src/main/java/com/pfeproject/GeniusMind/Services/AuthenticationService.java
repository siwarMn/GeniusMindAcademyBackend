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
import com.pfeproject.GeniusMind.dto.UpdateProfileRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.Arrays;
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

    /* ========================= REGISTER ========================= */

    @Transactional
    public void register(MultipartFile file, RegisterRequest request) throws Exception {

        if (repository.findUserByEmail(request.getEmail()).isPresent()) {
            throw new UserExistException("User already exists");
        }

        String imageBase64 = null;
        if (file != null && !file.isEmpty()) {
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            log.info("Uploading image: {}", fileName);
            imageBase64 = Base64.getEncoder().encodeToString(file.getBytes());
        }

        User user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .niveau(request.getNiveau())
                .image(imageBase64.getBytes())
                .build();

        repository.save(user);

        String jwtToken = jwtService.generateToken(user);
        String link = "http://localhost:4200/activated?token=" + jwtToken;

        emailService.sendSimpleEmail(
                user.getEmail(),
                "Please confirm your account",
                emailService.buildEmail(user.getFirstname(), link)
        );
    }

    /* ========================= AUTHENTICATE ========================= */

    public AuthenticationResponse authenticate(AuthenticationRequest request) {

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Invalid email or password");
        }

        User user = repository.findUserByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        String token = jwtService.generateToken(user);

        return AuthenticationResponse.builder()
                .token(token)
                .role(user.getRole())
                .Nom(user.getFirstname())
                .prenom(user.getLastname())
                .image(Arrays.toString(user.getImage()))
                .build();
    }

    /* ========================= FORGOT PASSWORD ========================= */

    public String forgotpassword(ForgotPassword email) {

        User user = repository.findUserByEmail(email.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        String token = jwtService.generateToken(user);
        String link = "http://localhost:4200/reset-password?token=" + token;

        emailService.sendSimpleEmail(
                user.getEmail(),
                "Password Reset",
                emailService.buildEmail(user.getFirstname(), link)
        );

        return "Email sent";
    }

    /* ========================= RESET PASSWORD ========================= */

    public void resetpassword(ResetPassword password, String token) {

        if (jwtService.isTokenExpired(token)) {
            throw new IllegalStateException("Expired token");
        }

        String email = jwtService.extractUsername(token);

        User user = repository.findUserByEmail(email)
                .orElseThrow(() -> new IllegalStateException("Invalid token"));

        user.setPassword(passwordEncoder.encode(password.getPassword()));
        repository.save(user);
    }

    /* ========================= USERS ========================= */

    public List<User> getAccounts() {
        return repository.findUsersByRole(Role.ENSEI);
    }

    public long getnbByRole(Role role) {
        return repository.countByRole(role);
    }

    /* ========================= UPDATE PROFILE ========================= */

    @Transactional
    public void updateUserProfile(MultipartFile file, UpdateProfileRequest request, Principal connectedUser) throws Exception {

        // Find user by connected principal (current secure user)
        User existingUser = repository.findUserByEmail(connectedUser.getName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (request.getFirstname() != null)
            existingUser.setFirstname(request.getFirstname());

        if (request.getLastname() != null)
            existingUser.setLastname(request.getLastname());

        if (request.getNiveau() != null)
            existingUser.setNiveau(request.getNiveau());

        // Email Update Logic
        if (request.getEmail() != null && !request.getEmail().equals(existingUser.getEmail())) {
            // Check if new email is taken
            if (repository.findUserByEmail(request.getEmail()).isPresent()) {
                throw new UserExistException("Email already in use");
            }
            existingUser.setEmail(request.getEmail());
        }

        // Password Update Logic
        if (request.getNewPassword() != null && !request.getNewPassword().isEmpty()) {
            if (request.getOldPassword() == null || request.getOldPassword().isEmpty()) {
                throw new IllegalStateException("Old password is required");
            }
            if (!passwordEncoder.matches(request.getOldPassword(), existingUser.getPassword())) {
                throw new IllegalStateException("Invalid old password");
            }
            if (!request.getNewPassword().equals(request.getConfirmPassword())) {
                throw new IllegalStateException("Passwords do not match");
            }
            existingUser.setPassword(passwordEncoder.encode(request.getNewPassword()));
        }

        // Image Update Logic
        if (file != null && !file.isEmpty()) {
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            log.info("Uploading image: {}", fileName);
            String imageBase64 = Base64.getEncoder().encodeToString(file.getBytes());
            existingUser.setImage(imageBase64.getBytes());
        }

        repository.save(existingUser);
    }

    /* ========================= DELETE ========================= */

    public void deleteProfile(Integer idprofile) {
        repository.deleteById(idprofile);
    }
}
