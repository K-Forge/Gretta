package com.beautysalon.gretta.repository;

import com.beautysalon.gretta.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Integer> {
    
    List<Producto> findByStockGreaterThan(Integer stock);
    
    List<Producto> findByNombreContainingIgnoreCase(String nombre);
}
