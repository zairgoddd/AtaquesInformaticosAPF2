package com.utp.ataquesinformaticos.repository;

import com.utp.ataquesinformaticos.model.*;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AmenazaRepository extends JpaRepository<Amenaza, Integer> {
    // Métodos básicos de CRUD ya están incluidos en JpaRepository
     // Obtener las últimas 10 amenazas por fecha de detección descendente
    List<Amenaza> findTop10ByOrderByFechaDeteccionDesc();

    // Obtener amenazas por nivel de riesgo
    List<Amenaza> findByNivelRiesgo(NivelRiesgo nivelRiesgo);

    // Buscar amenazas por tipo (ignorando mayúsculas/minúsculas)
    List<Amenaza> findByTipoContainingIgnoreCase(String tipo);

    // Obtener todas las amenazas ordenadas por ID descendente, con paginación
    Page<Amenaza> findAllByOrderByIdDesc(Pageable pageable);
    
     // Contar por nivel de riesgo
    int countByNivelRiesgo(NivelRiesgo nivelRiesgo);

    
    // Contar amenazas por tipo
    @Query("SELECT a.tipo, COUNT(a) FROM Amenaza a GROUP BY a.tipo")
    List<Object[]> countByTipo();
    

}
