package com.beautysalon.gretta.repository;

import com.beautysalon.gretta.entity.Servicio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServicioRepository extends JpaRepository<Servicio, Integer> {
    
    List<Servicio> findByActivoTrue();
    
    List<Servicio> findByNombreContainingIgnoreCase(String nombre);
}
