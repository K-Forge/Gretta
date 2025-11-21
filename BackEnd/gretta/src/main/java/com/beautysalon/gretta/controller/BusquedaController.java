package com.beautysalon.gretta.controller;

import com.beautysalon.gretta.dto.busqueda.BusquedaCriteria;
import com.beautysalon.gretta.dto.busqueda.ResultadoBusquedaResponse;
import com.beautysalon.gretta.dto.cita.CitaResponse;
import com.beautysalon.gretta.dto.producto.ProductoResponse;
import com.beautysalon.gretta.dto.usuario.UsuarioResponse;
import com.beautysalon.gretta.dto.venta.VentaResponse;
import com.beautysalon.gretta.service.BusquedaAvanzadaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/busqueda")
@RequiredArgsConstructor
public class BusquedaController {

    private final BusquedaAvanzadaService busquedaService;

    @PostMapping("/citas")
    @PreAuthorize("hasRole('ESTILISTA')")
    public ResponseEntity<ResultadoBusquedaResponse<CitaResponse>> buscarCitas(
            @RequestBody BusquedaCriteria criteria) {
        return ResponseEntity.ok(busquedaService.buscarCitas(criteria));
    }

    @PostMapping("/ventas")
    @PreAuthorize("hasRole('ESTILISTA')")
    public ResponseEntity<ResultadoBusquedaResponse<VentaResponse>> buscarVentas(
            @RequestBody BusquedaCriteria criteria) {
        return ResponseEntity.ok(busquedaService.buscarVentas(criteria));
    }

    @PostMapping("/productos")
    @PreAuthorize("hasAnyRole('CLIENTE', 'ESTILISTA')")
    public ResponseEntity<ResultadoBusquedaResponse<ProductoResponse>> buscarProductos(
            @RequestBody BusquedaCriteria criteria) {
        return ResponseEntity.ok(busquedaService.buscarProductos(criteria));
    }

    @PostMapping("/usuarios")
    @PreAuthorize("hasRole('ESTILISTA')")
    public ResponseEntity<ResultadoBusquedaResponse<UsuarioResponse>> buscarUsuarios(
            @RequestBody BusquedaCriteria criteria) {
        return ResponseEntity.ok(busquedaService.buscarUsuarios(criteria));
    }
}
