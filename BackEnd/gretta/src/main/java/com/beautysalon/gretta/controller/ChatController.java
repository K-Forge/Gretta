package com.beautysalon.gretta.controller;

import com.beautysalon.gretta.dto.chat.*;
import com.beautysalon.gretta.service.ChatService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @PostMapping("/conversacion")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<ChatConversacionResponse> iniciarConversacion(
            @Valid @RequestBody ChatConversacionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(chatService.iniciarConversacion(request));
    }

    @PostMapping("/mensaje")
    @PreAuthorize("hasAnyRole('CLIENTE', 'ESTILISTA')")
    public ResponseEntity<ChatMensajeResponse> enviarMensaje(
            @Valid @RequestBody ChatMensajeRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(chatService.enviarMensaje(request));
    }

    @GetMapping("/conversaciones/cliente/{idCliente}")
    @PreAuthorize("hasAnyRole('CLIENTE', 'ESTILISTA')")
    public ResponseEntity<List<ChatConversacionResponse>> obtenerConversacionesPorCliente(
            @PathVariable Integer idCliente) {
        return ResponseEntity.ok(chatService.obtenerConversacionesPorCliente(idCliente));
    }

    @GetMapping("/conversaciones")
    @PreAuthorize("hasRole('ESTILISTA')")
    public ResponseEntity<List<ChatConversacionResponse>> obtenerTodasConversaciones() {
        return ResponseEntity.ok(chatService.obtenerTodasConversaciones());
    }

    @GetMapping("/conversacion/{idConversacion}")
    @PreAuthorize("hasAnyRole('CLIENTE', 'ESTILISTA')")
    public ResponseEntity<ConversacionDetalleResponse> obtenerConversacionDetalle(
            @PathVariable Integer idConversacion) {
        return ResponseEntity.ok(chatService.obtenerConversacionDetalle(idConversacion));
    }

    @GetMapping("/conversacion/{idConversacion}/mensajes")
    @PreAuthorize("hasAnyRole('CLIENTE', 'ESTILISTA')")
    public ResponseEntity<List<ChatMensajeResponse>> obtenerMensajesPorConversacion(
            @PathVariable Integer idConversacion) {
        return ResponseEntity.ok(chatService.obtenerMensajesPorConversacion(idConversacion));
    }

    @DeleteMapping("/conversacion/{idConversacion}")
    @PreAuthorize("hasAnyRole('CLIENTE', 'ESTILISTA')")
    public ResponseEntity<Void> eliminarConversacion(@PathVariable Integer idConversacion) {
        chatService.eliminarConversacion(idConversacion);
        return ResponseEntity.noContent().build();
    }
}
