package service;

import lombok.RequiredArgsConstructor;
import model.Usuario;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import repository.UsuarioRepository;

@Service
@RequiredArgsConstructor
public class UsuarioDetailsService implements UserDetailsService {

    private final UsuarioRepository repository;

    @Override
    public UserDetails loadUserByUsername(String email) {
        Usuario usuario = repository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException("Usuário não encontrado")
                );

        return User.withUsername(usuario.getEmail())
                .password(usuario.getSenhaHash())
                .roles(usuario.getPapel())
                .disabled(!"S".equals(usuario.getAtivo()))
                .build();
    }
}