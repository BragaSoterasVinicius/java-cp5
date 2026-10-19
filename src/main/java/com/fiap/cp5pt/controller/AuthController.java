package com.fiap.cp5pt.controller;

import com.fiap.cp5pt.dto.request.LoginRequest;
import com.fiap.cp5pt.dto.request.RegistroRequest;
import com.fiap.cp5pt.dto.response.AuthResponse;
import com.fiap.cp5pt.exception.EmailJaCadastradoException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import com.fiap.cp5pt.model.Usuario;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.bind.annotation.*;
import com.fiap.cp5pt.repository.UsuarioRepository;
import com.fiap.cp5pt.service.AuthService;


@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UsuarioRepository usuarioRepository;
    private final AuthenticationManager authenticationManager;
    private final SecurityContextRepository contextRepository;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(
            @Valid @RequestBody RegistroRequest request) {

        AuthResponse response = authService.register(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @Valid @RequestBody LoginRequest request,
            HttpServletRequest httpRequest,
            HttpServletResponse httpResponse) {

        try {
            Authentication authentication =
                    authenticationManager.authenticate(
                            new UsernamePasswordAuthenticationToken(
                                    request.email().trim().toLowerCase(),
                                    request.senha()
                            )
                    );

            SecurityContext context =
                    SecurityContextHolder.createEmptyContext();

            context.setAuthentication(authentication);
            SecurityContextHolder.setContext(context);

            contextRepository.saveContext(
                    context, httpRequest, httpResponse
            );

            Usuario usuario = usuarioRepository
                    .findByEmail(authentication.getName())
                    .orElseThrow();

            return ResponseEntity.ok(
                    new AuthResponse(
                            usuario.getId(),
                            usuario.getNome(),
                            usuario.getEmail(),
                            usuario.getPapel()
                    )
            );

        } catch (AuthenticationException exception) {
            SecurityContextHolder.clearContext();

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Email ou senha inválidos");
        }
    }

    @ExceptionHandler(EmailJaCadastradoException.class)
    public ResponseEntity<String> handleDuplicateEmail(
            EmailJaCadastradoException exception) {

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body("Este email já está cadastrado");
    }

}