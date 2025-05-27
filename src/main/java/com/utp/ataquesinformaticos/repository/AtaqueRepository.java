package com.utp.ataquesinformaticos.repository;

import com.utp.ataquesinformaticos.model.*;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AtaqueRepository extends JpaRepository<Ataque, Integer> {
    // Métodos básicos de CRUD ya están incluidos en JpaRepository
    
      // Obtener los 10 ataques más recientes
    List<Ataque> findTop10ByOrderByFechaEventoDesc();

    // Obtener todos los ataques ordenados por ID descendente, con paginación
    Page<Ataque> findAllByOrderByIdDesc(Pageable pageable);

    // Obtener ataques por el ID de la amenaza asociada
    List<Ataque> findByAmenazaId(Integer amenazaId);

    // Buscar ataques por severidad
    List<Ataque> findBySeveridad(Severidad severidad);

    // Buscar ataques por sistema afectado (ignorando mayúsculas)
    List<Ataque> findBySistemaAfectadoContainingIgnoreCase(String sistemaAfectado);
    
    
    // Encontrar por amenaza
    List<Ataque> findByAmenaza(Amenaza amenaza);
    
    // Contar ataques por tipo
    @Query("SELECT a.tipo, COUNT(a) FROM Ataque a GROUP BY a.tipo")
    List<Object[]> countByTipo();
    
    // Contar ataques por mes
    @Query("SELECT MONTHNAME(a.fechaEvento), COUNT(a) FROM Ataque a GROUP BY MONTH(a.fechaEvento), YEAR(a.fechaEvento)")
    List<Object[]> countByMonth();
 
}
