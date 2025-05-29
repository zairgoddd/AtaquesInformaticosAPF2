package com.utp.ataquesinformaticos.repository;

import com.utp.ataquesinformaticos.model.Amenaza;
import com.utp.ataquesinformaticos.model.Ataque;
import com.utp.ataquesinformaticos.model.Severidad;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AtaqueRepository extends JpaRepository<Ataque, Integer> {

    // 1. Obtener los 10 ataques más recientes
    @Query("SELECT a FROM Ataque a ORDER BY a.fechaEvento DESC")
    List<Ataque> findTop10ByOrderByFechaEventoDesc();

    // 2. Obtener todos los ataques ordenados por ID descendente, con paginación
    @Query("SELECT a FROM Ataque a ORDER BY a.id DESC")
    Page<Ataque> findAllByOrderByIdDesc(Pageable pageable);

    // 3. Obtener ataques por el ID de la amenaza asociada
    @Query("SELECT a FROM Ataque a WHERE a.amenaza.id = :amenazaId")
    List<Ataque> findByAmenazaId(@Param("amenazaId") Integer amenazaId);

    // 4. Buscar ataques por severidad
    @Query("SELECT a FROM Ataque a WHERE a.severidad = :severidad")
    List<Ataque> findBySeveridad(@Param("severidad") Severidad severidad);

    // 5. Buscar ataques por sistema afectado (ignorando mayúsculas)
    @Query("SELECT a FROM Ataque a WHERE LOWER(a.sistemaAfectado) LIKE LOWER(CONCAT('%', :sistemaAfectado, '%'))")
    List<Ataque> findBySistemaAfectadoContainingIgnoreCase(@Param("sistemaAfectado") String sistemaAfectado);

    // 6. Encontrar por amenaza
    @Query("SELECT a FROM Ataque a WHERE a.amenaza = :amenaza")
    List<Ataque> findByAmenaza(@Param("amenaza") Amenaza amenaza);

    // 7. Contar ataques por tipo (ya con JPQL)
    @Query("SELECT a.tipo, COUNT(a) FROM Ataque a GROUP BY a.tipo")
    List<Object[]> countByTipo();

    // 8. Contar ataques por mes (ya con JPQL)
    @Query("SELECT MONTHNAME(a.fechaEvento), COUNT(a) FROM Ataque a GROUP BY MONTH(a.fechaEvento), YEAR(a.fechaEvento)")
    List<Object[]> countByMonth();
}
