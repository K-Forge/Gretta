package com.beautysalon.gretta.controller;

import com.beautysalon.gretta.entity.Estilista;
import com.beautysalon.gretta.service.EstilistaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/estilistas")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class EstilistaController {

    private final EstilistaService estilistaService;

    @GetMapping
    @PreAuthorize("hasAnyRole('CLIENTE', 'ESTILISTA')")
    public ResponseEntity<List<Estilista>> obtenerTodos() {
        return ResponseEntity.ok(estilistaService.obtenerTodos());
    }
}
