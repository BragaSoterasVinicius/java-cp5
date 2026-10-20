package com.fiap.cp5pt.service;

import com.fiap.cp5pt.model.UsuarioDetails;
import lombok.RequiredArgsConstructor;
import com.fiap.cp5pt.model.Usuario;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.fiap.cp5pt.repository.UsuarioRepository;

@Service
@RequiredArgsConstructor
public class UsuarioDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String email) {
        String normalizedEmail = email.trim().toLowerCase();

        return usuarioRepository.findByEmail(normalizedEmail)
                .map(UsuarioDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Usuário não encontrado: " + normalizedEmail
                ));
    }
}