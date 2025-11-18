package com.beautysalon.gretta.repository;

import com.beautysalon.gretta.entity.DetalleVenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DetalleVentaRepository extends JpaRepository<DetalleVenta, Integer> {
    
    List<DetalleVenta> findByVenta_IdVenta(Integer idVenta);
    
    @Query("SELECT d FROM DetalleVenta d WHERE d.producto.idProducto = :idProducto")
    List<DetalleVenta> findByProducto(Integer idProducto);
    
    @Query("SELECT d.producto.nombre, SUM(d.cantidad) as total " +
           "FROM DetalleVenta d " +
           "GROUP BY d.producto.idProducto, d.producto.nombre " +
           "ORDER BY total DESC")
    List<Object[]> getProductosMasVendidos();
}
