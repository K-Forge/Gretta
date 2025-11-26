package com.beautysalon.gretta.service;

import com.beautysalon.gretta.entity.Usuario;
import com.beautysalon.gretta.repository.UsuarioRepository;
import com.beautysalon.gretta.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String correo) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con correo: " + correo));

        if (!usuario.getActivo()) {
            throw new UsernameNotFoundException("Usuario inactivo: " + correo);
        }

        return new CustomUserDetails(usuario);
    }
}
