package com.beautysalon.gretta.service;

import com.beautysalon.gretta.dto.usuario.ActualizarPerfilRequest;
import com.beautysalon.gretta.dto.usuario.ActualizarUsuarioRequest;
import com.beautysalon.gretta.dto.usuario.CambiarContrasenaRequest;
import com.beautysalon.gretta.dto.usuario.UsuarioResponse;
import com.beautysalon.gretta.entity.Cliente;
import com.beautysalon.gretta.entity.Estilista;
import com.beautysalon.gretta.entity.Usuario;
import com.beautysalon.gretta.entity.enums.RolUsuario;
import com.beautysalon.gretta.repository.ClienteRepository;
import com.beautysalon.gretta.repository.EstilistaRepository;
import com.beautysalon.gretta.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final ClienteRepository clienteRepository;
    private final EstilistaRepository estilistaRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public UsuarioResponse obtenerPerfil(Integer idUsuario) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + idUsuario));
        return convertirAResponse(usuario);
    }

    @Transactional(readOnly = true)
    public UsuarioResponse obtenerPorCorreo(String correo) {
        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con correo: " + correo));
        return convertirAResponse(usuario);
    }

    @Transactional(readOnly = true)
    public List<UsuarioResponse> obtenerTodos() {
        return usuarioRepository.findAll().stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<UsuarioResponse> obtenerPorRol(RolUsuario rol) {
        return usuarioRepository.findByRol(rol).stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<UsuarioResponse> obtenerActivos() {
        return usuarioRepository.findByActivo(true).stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<UsuarioResponse> obtenerInactivos() {
        return usuarioRepository.findByActivo(false).stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<UsuarioResponse> buscarPorNombre(String nombre) {
        return usuarioRepository.findByNombreContainingIgnoreCaseOrApellidoContainingIgnoreCase(nombre, nombre).stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public UsuarioResponse actualizarPerfil(Integer idUsuario, ActualizarPerfilRequest request) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + idUsuario));

        if (request.getNombre() != null) {
            usuario.setNombre(request.getNombre());
        }
        if (request.getApellido() != null) {
            usuario.setApellido(request.getApellido());
        }
        if (request.getCorreo() != null) {
            if (!request.getCorreo().equals(usuario.getCorreo()) && 
                usuarioRepository.findByCorreo(request.getCorreo()).isPresent()) {
                throw new RuntimeException("El correo ya está en uso");
            }
            usuario.setCorreo(request.getCorreo());
        }
        if (request.getTelefono() != null) {
            usuario.setTelefono(request.getTelefono());
        }
        if (request.getCanalPreferido() != null) {
            usuario.setCanalPreferido(request.getCanalPreferido());
        }

        usuario.setFechaModificacion(LocalDateTime.now());
        usuario = usuarioRepository.save(usuario);
        return convertirAResponse(usuario);
    }

    @Transactional
    public UsuarioResponse actualizarUsuario(Integer idUsuario, ActualizarUsuarioRequest request) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + idUsuario));

        usuario.setNombre(request.getNombre());
        usuario.setApellido(request.getApellido());
        
        if (!request.getCorreo().equals(usuario.getCorreo()) && 
            usuarioRepository.findByCorreo(request.getCorreo()).isPresent()) {
            throw new RuntimeException("El correo ya está en uso");
        }
        usuario.setCorreo(request.getCorreo());
        
        usuario.setTelefono(request.getTelefono());
        
        if (request.getTipoDocumento() != null) {
            usuario.setTipoDocumento(request.getTipoDocumento());
        }
        if (request.getNumeroDocumento() != null) {
            if (!request.getNumeroDocumento().equals(usuario.getNumeroDocumento()) && 
                usuarioRepository.findByNumeroDocumento(request.getNumeroDocumento()).isPresent()) {
                throw new RuntimeException("El número de documento ya está en uso");
            }
            usuario.setNumeroDocumento(request.getNumeroDocumento());
        }
        if (request.getRol() != null) {
            usuario.setRol(request.getRol());
        }
        if (request.getCanalPreferido() != null) {
            usuario.setCanalPreferido(request.getCanalPreferido());
        }
        if (request.getActivo() != null) {
            usuario.setActivo(request.getActivo());
        }

        usuario.setFechaModificacion(LocalDateTime.now());
        usuario = usuarioRepository.save(usuario);
        return convertirAResponse(usuario);
    }

    @Transactional
    public void cambiarContrasena(Integer idUsuario, CambiarContrasenaRequest request) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + idUsuario));

        if (!passwordEncoder.matches(request.getContrasenaActual(), usuario.getContrasena())) {
            throw new RuntimeException("La contraseña actual es incorrecta");
        }

        if (!request.getContrasenaNueva().equals(request.getConfirmacionContrasena())) {
            throw new RuntimeException("La nueva contraseña y su confirmación no coinciden");
        }

        usuario.setContrasena(passwordEncoder.encode(request.getContrasenaNueva()));
        usuario.setFechaModificacion(LocalDateTime.now());
        usuarioRepository.save(usuario);
    }

    @Transactional
    public UsuarioResponse activarUsuario(Integer idUsuario) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + idUsuario));

        usuario.setActivo(true);
        usuario.setFechaModificacion(LocalDateTime.now());
        usuario = usuarioRepository.save(usuario);
        return convertirAResponse(usuario);
    }

    @Transactional
    public UsuarioResponse desactivarUsuario(Integer idUsuario) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + idUsuario));

        usuario.setActivo(false);
        usuario.setFechaModificacion(LocalDateTime.now());
        usuario = usuarioRepository.save(usuario);
        return convertirAResponse(usuario);
    }

    @Transactional
    public void eliminarUsuario(Integer idUsuario) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + idUsuario));

        // Eliminar registros relacionados según el rol
        if (usuario.getRol() == RolUsuario.CLIENTE) {
            clienteRepository.findByUsuario(usuario).ifPresent(clienteRepository::delete);
        } else if (usuario.getRol() == RolUsuario.ESTILISTA) {
            estilistaRepository.findByUsuario(usuario).ifPresent(estilistaRepository::delete);
        }

        usuarioRepository.delete(usuario);
    }

    @Transactional(readOnly = true)
    public Long contarTotal() {
        return usuarioRepository.count();
    }

    @Transactional(readOnly = true)
    public Long contarPorRol(RolUsuario rol) {
        return usuarioRepository.countByRol(rol);
    }

    @Transactional(readOnly = true)
    public Long contarActivos() {
        return usuarioRepository.countByActivo(true);
    }

    private UsuarioResponse convertirAResponse(Usuario usuario) {
        return UsuarioResponse.builder()
                .idUsuario(usuario.getIdUsuario())
                .nombre(usuario.getNombre())
                .apellido(usuario.getApellido())
                .correo(usuario.getCorreo())
                .telefono(usuario.getTelefono())
                .tipoDocumento(usuario.getTipoDocumento())
                .numeroDocumento(usuario.getNumeroDocumento())
                .rol(usuario.getRol())
                .canalPreferido(usuario.getCanalPreferido())
                .activo(usuario.getActivo())
                .fechaCreacion(usuario.getFechaCreacion())
                .fechaModificacion(usuario.getFechaModificacion())
                .build();
    }
}
