package com.beautysalon.gretta.controller;

import com.beautysalon.gretta.dto.usuario.ActualizarPerfilRequest;
import com.beautysalon.gretta.dto.usuario.ActualizarUsuarioRequest;
import com.beautysalon.gretta.dto.usuario.CambiarContrasenaRequest;
import com.beautysalon.gretta.dto.usuario.UsuarioResponse;
import com.beautysalon.gretta.entity.enums.RolUsuario;
import com.beautysalon.gretta.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('CLIENTE', 'ESTILISTA')")
    public ResponseEntity<UsuarioResponse> obtenerUsuario(@PathVariable Integer id) {
        return ResponseEntity.ok(usuarioService.obtenerPerfil(id));
    }

    @GetMapping("/correo/{correo}")
    @PreAuthorize("hasRole('ESTILISTA')")
    public ResponseEntity<UsuarioResponse> obtenerPorCorreo(@PathVariable String correo) {
        return ResponseEntity.ok(usuarioService.obtenerPorCorreo(correo));
    }

    @GetMapping
    @PreAuthorize("hasRole('ESTILISTA')")
    public ResponseEntity<List<UsuarioResponse>> obtenerTodos() {
        return ResponseEntity.ok(usuarioService.obtenerTodos());
    }

    @GetMapping("/rol/{rol}")
    @PreAuthorize("hasRole('ESTILISTA')")
    public ResponseEntity<List<UsuarioResponse>> obtenerPorRol(@PathVariable RolUsuario rol) {
        return ResponseEntity.ok(usuarioService.obtenerPorRol(rol));
    }

    @GetMapping("/activos")
    @PreAuthorize("hasRole('ESTILISTA')")
    public ResponseEntity<List<UsuarioResponse>> obtenerActivos() {
        return ResponseEntity.ok(usuarioService.obtenerActivos());
    }

    @GetMapping("/inactivos")
    @PreAuthorize("hasRole('ESTILISTA')")
    public ResponseEntity<List<UsuarioResponse>> obtenerInactivos() {
        return ResponseEntity.ok(usuarioService.obtenerInactivos());
    }

    @GetMapping("/buscar")
    @PreAuthorize("hasRole('ESTILISTA')")
    public ResponseEntity<List<UsuarioResponse>> buscarPorNombre(@RequestParam String nombre) {
        return ResponseEntity.ok(usuarioService.buscarPorNombre(nombre));
    }

    @PutMapping("/{id}/perfil")
    @PreAuthorize("hasAnyRole('CLIENTE', 'ESTILISTA')")
    public ResponseEntity<UsuarioResponse> actualizarPerfil(
            @PathVariable Integer id,
            @Valid @RequestBody ActualizarPerfilRequest request) {
        return ResponseEntity.ok(usuarioService.actualizarPerfil(id, request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ESTILISTA')")
    public ResponseEntity<UsuarioResponse> actualizarUsuario(
            @PathVariable Integer id,
            @Valid @RequestBody ActualizarUsuarioRequest request) {
        return ResponseEntity.ok(usuarioService.actualizarUsuario(id, request));
    }

    @PutMapping("/{id}/cambiar-contrasena")
    @PreAuthorize("hasAnyRole('CLIENTE', 'ESTILISTA')")
    public ResponseEntity<Void> cambiarContrasena(
            @PathVariable Integer id,
            @Valid @RequestBody CambiarContrasenaRequest request) {
        usuarioService.cambiarContrasena(id, request);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/activar")
    @PreAuthorize("hasRole('ESTILISTA')")
    public ResponseEntity<UsuarioResponse> activarUsuario(@PathVariable Integer id) {
        return ResponseEntity.ok(usuarioService.activarUsuario(id));
    }

    @PutMapping("/{id}/desactivar")
    @PreAuthorize("hasRole('ESTILISTA')")
    public ResponseEntity<UsuarioResponse> desactivarUsuario(@PathVariable Integer id) {
        return ResponseEntity.ok(usuarioService.desactivarUsuario(id));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ESTILISTA')")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Integer id) {
        usuarioService.eliminarUsuario(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/estadisticas")
    @PreAuthorize("hasRole('ESTILISTA')")
    public ResponseEntity<Map<String, Long>> obtenerEstadisticas() {
        return ResponseEntity.ok(Map.of(
                "total", usuarioService.contarTotal(),
                "clientes", usuarioService.contarPorRol(RolUsuario.CLIENTE),
                "estilistas", usuarioService.contarPorRol(RolUsuario.ESTILISTA),
                "activos", usuarioService.contarActivos()
        ));
    }
}
