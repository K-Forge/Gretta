package com.beautysalon.gretta.repository;

import com.beautysalon.gretta.entity.Promocion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PromocionRepository extends JpaRepository<Promocion, Integer> {
    
    List<Promocion> findByActivoTrue();
    
    @Query("SELECT p FROM Promocion p WHERE p.activo = true AND p.fechaInicio <= :fecha AND p.fechaFin >= :fecha")
    List<Promocion> findPromocionesVigentes(LocalDateTime fecha);
    
    // Métodos adicionales para validaciones
    List<Promocion> findByActivo(boolean activo);
    
    List<Promocion> findByActivoAndFechaInicioBeforeAndFechaFinAfter(boolean activo, LocalDateTime fechaInicio, LocalDateTime fechaFin);
    
    Long countByActivoAndFechaInicioBeforeAndFechaFinAfter(boolean activo, LocalDateTime fechaInicio, LocalDateTime fechaFin);
}
