package com.beautysalon.gretta.repository;

import com.beautysalon.gretta.entity.Cita;
import com.beautysalon.gretta.entity.enums.EstadoCita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CitaRepository extends JpaRepository<Cita, Integer> {
    
    List<Cita> findByCliente_IdCliente(Integer idCliente);
    
    List<Cita> findByEstilista_IdEstilista(Integer idEstilista);
    
    List<Cita> findByEstado(EstadoCita estado);
    
    @Query("SELECT c FROM Cita c WHERE c.estilista.idEstilista = :idEstilista AND c.fechaCita = :fecha")
    List<Cita> findByEstilistaAndFecha(Integer idEstilista, LocalDateTime fecha);
    
    @Query("SELECT c FROM Cita c WHERE c.cliente.idCliente = :idCliente ORDER BY c.fechaCita DESC")
    List<Cita> findByClienteOrderByFechaDesc(Integer idCliente);
}
