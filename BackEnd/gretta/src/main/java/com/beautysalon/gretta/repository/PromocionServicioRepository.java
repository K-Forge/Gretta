package com.beautysalon.gretta.repository;

import com.beautysalon.gretta.entity.PromocionServicio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PromocionServicioRepository extends JpaRepository<PromocionServicio, Integer> {

    @Query("SELECT ps FROM PromocionServicio ps WHERE ps.promocion.idPromocion = :idPromocion")
    List<PromocionServicio> findByPromocion(@Param("idPromocion") Integer idPromocion);

    @Query("SELECT ps FROM PromocionServicio ps WHERE ps.servicio.idServicio = :idServicio")
    List<PromocionServicio> findByServicio(@Param("idServicio") Integer idServicio);

    @Query("SELECT ps FROM PromocionServicio ps WHERE ps.promocion.idPromocion = :idPromocion " +
           "AND ps.servicio.idServicio = :idServicio")
    Optional<PromocionServicio> findByPromocionAndServicio(
            @Param("idPromocion") Integer idPromocion,
            @Param("idServicio") Integer idServicio);

    @Query("SELECT COUNT(ps) > 0 FROM PromocionServicio ps WHERE ps.promocion.idPromocion = :idPromocion " +
           "AND ps.servicio.idServicio = :idServicio")
    boolean existsByPromocionAndServicio(
            @Param("idPromocion") Integer idPromocion,
            @Param("idServicio") Integer idServicio);

    @Query("SELECT COUNT(ps) FROM PromocionServicio ps WHERE ps.promocion.idPromocion = :idPromocion")
    Long countByPromocion(@Param("idPromocion") Integer idPromocion);

    @Query("SELECT COUNT(ps) FROM PromocionServicio ps WHERE ps.servicio.idServicio = :idServicio")
    Long countByServicio(@Param("idServicio") Integer idServicio);

    @Query("DELETE FROM PromocionServicio ps WHERE ps.promocion.idPromocion = :idPromocion")
    void deleteByPromocion(@Param("idPromocion") Integer idPromocion);

    @Query("DELETE FROM PromocionServicio ps WHERE ps.servicio.idServicio = :idServicio")
    void deleteByServicio(@Param("idServicio") Integer idServicio);
}
