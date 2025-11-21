package com.beautysalon.gretta.controller;

import com.beautysalon.gretta.dto.notificacion.EnviarNotificacionMasivaRequest;
import com.beautysalon.gretta.dto.notificacion.NotificacionRequest;
import com.beautysalon.gretta.dto.notificacion.NotificacionResponse;
import com.beautysalon.gretta.entity.enums.EstadoNotificacion;
import com.beautysalon.gretta.entity.enums.TipoNotificacion;
import com.beautysalon.gretta.service.NotificacionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notificaciones")
@RequiredArgsConstructor
public class NotificacionController {

    private final NotificacionService notificacionService;

    @PostMapping
    @PreAuthorize("hasRole('ESTILISTA')")
    public ResponseEntity<NotificacionResponse> crearNotificacion(
            @Valid @RequestBody NotificacionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(notificacionService.crearNotificacion(request));
    }

    @PostMapping("/masiva")
    @PreAuthorize("hasRole('ESTILISTA')")
    public ResponseEntity<List<NotificacionResponse>> enviarNotificacionMasiva(
            @Valid @RequestBody EnviarNotificacionMasivaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(notificacionService.enviarNotificacionMasiva(request));
    }

    @PostMapping("/recordatorio-cita/{idCita}")
    @PreAuthorize("hasRole('ESTILISTA')")
    public ResponseEntity<NotificacionResponse> enviarRecordatorioCita(@PathVariable Integer idCita) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(notificacionService.enviarNotificacionRecordatorioCita(idCita));
    }

    @PostMapping("/promocion/{idPromocion}")
    @PreAuthorize("hasRole('ESTILISTA')")
    public ResponseEntity<List<NotificacionResponse>> notificarPromocion(@PathVariable Integer idPromocion) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(notificacionService.notificarPromocion(idPromocion));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('CLIENTE', 'ESTILISTA')")
    public ResponseEntity<NotificacionResponse> obtenerPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(notificacionService.obtenerPorId(id));
    }

    @GetMapping("/usuario/{idUsuario}")
    @PreAuthorize("hasAnyRole('CLIENTE', 'ESTILISTA')")
    public ResponseEntity<List<NotificacionResponse>> obtenerPorUsuario(@PathVariable Integer idUsuario) {
        return ResponseEntity.ok(notificacionService.obtenerPorUsuario(idUsuario));
    }

    @GetMapping("/usuario/{idUsuario}/estado/{estado}")
    @PreAuthorize("hasAnyRole('CLIENTE', 'ESTILISTA')")
    public ResponseEntity<List<NotificacionResponse>> obtenerPorUsuarioYEstado(
            @PathVariable Integer idUsuario,
            @PathVariable EstadoNotificacion estado) {
        return ResponseEntity.ok(notificacionService.obtenerPorUsuarioYEstado(idUsuario, estado));
    }

    @GetMapping("/tipo/{tipo}")
    @PreAuthorize("hasRole('ESTILISTA')")
    public ResponseEntity<List<NotificacionResponse>> obtenerPorTipo(@PathVariable TipoNotificacion tipo) {
        return ResponseEntity.ok(notificacionService.obtenerPorTipo(tipo));
    }

    @GetMapping("/pendientes")
    @PreAuthorize("hasRole('ESTILISTA')")
    public ResponseEntity<List<NotificacionResponse>> obtenerPendientes() {
        return ResponseEntity.ok(notificacionService.obtenerPendientes());
    }

    @GetMapping("/cita/{idCita}")
    @PreAuthorize("hasAnyRole('CLIENTE', 'ESTILISTA')")
    public ResponseEntity<List<NotificacionResponse>> obtenerPorCita(@PathVariable Integer idCita) {
        return ResponseEntity.ok(notificacionService.obtenerPorCita(idCita));
    }

    @GetMapping("/promocion/{idPromocion}")
    @PreAuthorize("hasRole('ESTILISTA')")
    public ResponseEntity<List<NotificacionResponse>> obtenerPorPromocion(@PathVariable Integer idPromocion) {
        return ResponseEntity.ok(notificacionService.obtenerPorPromocion(idPromocion));
    }

    @PutMapping("/{id}/enviada")
    @PreAuthorize("hasRole('ESTILISTA')")
    public ResponseEntity<NotificacionResponse> marcarComoEnviada(@PathVariable Integer id) {
        return ResponseEntity.ok(notificacionService.marcarComoEnviada(id));
    }

    @PutMapping("/{id}/entregada")
    @PreAuthorize("hasRole('ESTILISTA')")
    public ResponseEntity<NotificacionResponse> marcarComoEntregada(@PathVariable Integer id) {
        return ResponseEntity.ok(notificacionService.marcarComoEntregada(id));
    }

    @PutMapping("/{id}/fallida")
    @PreAuthorize("hasRole('ESTILISTA')")
    public ResponseEntity<NotificacionResponse> marcarComoFallida(@PathVariable Integer id) {
        return ResponseEntity.ok(notificacionService.marcarComoFallida(id));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ESTILISTA')")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        notificacionService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/usuario/{idUsuario}/estadisticas")
    @PreAuthorize("hasAnyRole('CLIENTE', 'ESTILISTA')")
    public ResponseEntity<Map<String, Long>> obtenerEstadisticasPorUsuario(@PathVariable Integer idUsuario) {
        return ResponseEntity.ok(Map.of(
                "total", notificacionService.contarPorUsuario(idUsuario),
                "pendientes", notificacionService.contarPorUsuarioYEstado(idUsuario, EstadoNotificacion.PENDIENTE),
                "enviadas", notificacionService.contarPorUsuarioYEstado(idUsuario, EstadoNotificacion.ENVIADA),
                "entregadas", notificacionService.contarPorUsuarioYEstado(idUsuario, EstadoNotificacion.ENTREGADA),
                "fallidas", notificacionService.contarPorUsuarioYEstado(idUsuario, EstadoNotificacion.FALLIDA)
        ));
    }
}
