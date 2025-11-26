package com.beautysalon.gretta.repository;

import com.beautysalon.gretta.entity.HistorialCliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface HistorialClienteRepository extends JpaRepository<HistorialCliente, Integer> {
    
    List<HistorialCliente> findByCliente_IdCliente(Integer idCliente);
    
    @Query("SELECT h FROM HistorialCliente h WHERE h.cliente.idCliente = :idCliente ORDER BY h.fechaServicio DESC")
    List<HistorialCliente> findByClienteOrderByFechaDesc(Integer idCliente);
    
    @Query("SELECT h FROM HistorialCliente h WHERE h.servicio.idServicio = :idServicio")
    List<HistorialCliente> findByServicio(Integer idServicio);
    
    @Query("SELECT h FROM HistorialCliente h WHERE h.cliente.idCliente = :idCliente " +
           "AND h.fechaServicio BETWEEN :fechaInicio AND :fechaFin ORDER BY h.fechaServicio DESC")
    List<HistorialCliente> findByClienteAndFechaBetween(Integer idCliente, LocalDateTime fechaInicio, LocalDateTime fechaFin);
    
    @Query("SELECT COUNT(h) FROM HistorialCliente h WHERE h.cliente.idCliente = :idCliente")
    Long countByCliente(Integer idCliente);
    
    @Query("SELECT h.servicio.nombre, COUNT(h) as total " +
           "FROM HistorialCliente h " +
           "WHERE h.cliente.idCliente = :idCliente " +
           "GROUP BY h.servicio.idServicio, h.servicio.nombre " +
           "ORDER BY total DESC")
    List<Object[]> getServiciosMasFrecuentesPorCliente(Integer idCliente);
    
    @Query("SELECT h.servicio.nombre, COUNT(h) as total " +
           "FROM HistorialCliente h " +
           "GROUP BY h.servicio.idServicio, h.servicio.nombre " +
           "ORDER BY total DESC")
    List<Object[]> getServiciosMasFrecuentes();
}
