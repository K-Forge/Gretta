package com.beautysalon.gretta.dto.busqueda;

import com.beautysalon.gretta.entity.enums.EstadoCita;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BusquedaCriteria {

    // Criterios generales
    private String palabraClave;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
    
    // Criterios de Cita
    private Integer idCliente;
    private Integer idEstilista;
    private Integer idServicio;
    private EstadoCita estadoCita;
    
    // Criterios de Venta
    private BigDecimal montoMinimo;
    private BigDecimal montoMaximo;
    
    // Criterios de Producto
    private Integer stockMinimo;
    private Integer stockMaximo;
    private BigDecimal precioMinimo;
    private BigDecimal precioMaximo;
    
    // Criterios de Usuario
    private Boolean activo;
    private String rol;
    
    // Ordenamiento
    private String ordenarPor; // fecha, nombre, precio, cantidad
    private String direccion; // ASC, DESC
    
    // Paginación
    private Integer pagina;
    private Integer tamanoPagina;
}
