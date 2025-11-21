package com.beautysalon.gretta.service;

import com.beautysalon.gretta.dto.auth.LoginRequest;
import com.beautysalon.gretta.dto.auth.LoginResponse;
import com.beautysalon.gretta.dto.auth.RegisterRequest;
import com.beautysalon.gretta.entity.Cliente;
import com.beautysalon.gretta.entity.Estilista;
import com.beautysalon.gretta.entity.Usuario;
import com.beautysalon.gretta.entity.enums.RolUsuario;
import com.beautysalon.gretta.repository.ClienteRepository;
import com.beautysalon.gretta.repository.EstilistaRepository;
import com.beautysalon.gretta.repository.UsuarioRepository;
import com.beautysalon.gretta.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final ClienteRepository clienteRepository;
    private final EstilistaRepository estilistaRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public LoginResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getCorreo(), request.getContrasena())
        );

        Usuario usuario = usuarioRepository.findByCorreo(request.getCorreo())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        String token = jwtUtil.generateToken(usuario.getCorreo());

        return LoginResponse.builder()
                .token(token)
                .tipo("Bearer")
                .idUsuario(usuario.getIdUsuario())
                .nombre(usuario.getNombre())
                .apellido(usuario.getApellido())
                .correo(usuario.getCorreo())
                .rol(usuario.getRol())
                .build();
    }

    @Transactional
    public String register(RegisterRequest request) {
        // Validar que el correo no esté registrado
        if (usuarioRepository.existsByCorreo(request.getCorreo())) {
            throw new RuntimeException("El correo ya está registrado");
        }

        // Validar que el número de documento no esté registrado
        if (usuarioRepository.existsByNumeroDocumento(request.getNumeroDocumento())) {
            throw new RuntimeException("El número de documento ya está registrado");
        }

        // Crear usuario
        Usuario usuario = new Usuario();
        usuario.setNombre(request.getNombre());
        usuario.setApellido(request.getApellido());
        usuario.setCorreo(request.getCorreo());
        usuario.setTelefono(request.getTelefono());
        usuario.setContrasena(passwordEncoder.encode(request.getContrasena()));
        usuario.setTipoDocumento(request.getTipoDocumento());
        usuario.setNumeroDocumento(request.getNumeroDocumento());
        usuario.setRol(request.getRol());
        usuario.setCanalPreferido(request.getCanalPreferido());
        usuario.setActivo(true);

        usuario = usuarioRepository.save(usuario);

        // Crear registro específico según el rol
        if (request.getRol() == RolUsuario.CLIENTE) {
            Cliente cliente = new Cliente();
            cliente.setUsuario(usuario);
            clienteRepository.save(cliente);
        } else if (request.getRol() == RolUsuario.ESTILISTA) {
            Estilista estilista = new Estilista();
            estilista.setUsuario(usuario);
            estilista.setEspecialidad(request.getEspecialidad());
            estilista.setDisponibilidad(request.getDisponibilidad() != null 
                ? request.getDisponibilidad() 
                : "Lunes a Viernes");
            estilistaRepository.save(estilista);
        }

        return "Usuario registrado exitosamente";
    }
}
