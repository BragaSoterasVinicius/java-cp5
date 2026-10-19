package com.fiap.cp5pt.service;


import com.fiap.cp5pt.dto.request.RegistroRequest;
import com.fiap.cp5pt.dto.response.AuthResponse;
import com.fiap.cp5pt.exception.EmailJaCadastradoException;
import lombok.RequiredArgsConstructor;
import com.fiap.cp5pt.model.Usuario;
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
}