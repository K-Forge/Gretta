package com.beautysalon.gretta.repository;

import com.beautysalon.gretta.entity.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface VentaRepository extends JpaRepository<Venta, Integer> {
    
    List<Venta> findByCliente_IdCliente(Integer idCliente);
    
    @Query("SELECT v FROM Venta v WHERE v.fechaVenta BETWEEN :fechaInicio AND :fechaFin ORDER BY v.fechaVenta DESC")
    List<Venta> findByFechaBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin);
    
    @Query("SELECT v FROM Venta v ORDER BY v.fechaVenta DESC")
    List<Venta> findAllOrderByFechaDesc();
    
    @Query("SELECT SUM(v.total) FROM Venta v WHERE v.fechaVenta BETWEEN :fechaInicio AND :fechaFin")
    Double getTotalVentasPorPeriodo(LocalDateTime fechaInicio, LocalDateTime fechaFin);
}
