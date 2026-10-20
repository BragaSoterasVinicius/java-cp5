package com.fiap.cp5pt.service;


import com.fiap.cp5pt.dto.request.LoginRequest;
import com.fiap.cp5pt.dto.request.RegistroRequest;
import com.fiap.cp5pt.dto.response.AuthResponse;
import com.fiap.cp5pt.exception.EmailJaCadastradoException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import com.fiap.cp5pt.model.Usuario;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.fiap.cp5pt.repository.UsuarioRepository;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository repository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public AuthResponse register(RegistroRequest request) {
        String email = request.email().trim().toLowerCase();

        if (repository.existsByEmail(email)) {
            throw new EmailJaCadastradoException();
        }

        Usuario usuario = new Usuario();
        usuario.setNome(request.nome().trim());
        usuario.setEmail(email);
        usuario.setSenhaHash(
                passwordEncoder.encode(request.senha())
        );
        usuario.setPapel("CLIENTE");
        usuario.setAtivo('S');

        Usuario salvo = repository.save(usuario);

        return new AuthResponse(
                salvo.getId(),
                salvo.getNome(),
                salvo.getEmail(),
                salvo.getPapel()
        );
    }

    public AuthResponse registerAdmin(@Valid RegistroRequest request) {
        if (repository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("Email já cadastrado");
        }

        Usuario usuario = new Usuario();
        usuario.setNome(request.nome());
        usuario.setEmail(request.email());
        usuario.setSenhaHash(passwordEncoder.encode(request.senha()));
        usuario.setPapel("ADMIN");

        usuario = repository.save(usuario);

        return new AuthResponse(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getPapel()
        );
    }

    public AuthResponse login(LoginRequest request) {
        String email = request.email().trim().toLowerCase();

        Usuario usuario = repository.findByEmail(email)
                .orElseThrow(() ->
                        new BadCredentialsException("Email ou senha inválidos"));

        if (!passwordEncoder.matches(
                request.senha(),
                usuario.getSenhaHash())) {
            throw new BadCredentialsException("Email ou senha inválidos");
        }

        if (usuario.getAtivo() != 'S') {
            throw new BadCredentialsException("Usuário inativo");
        }

        return new AuthResponse(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getPapel()
        );
    }
}