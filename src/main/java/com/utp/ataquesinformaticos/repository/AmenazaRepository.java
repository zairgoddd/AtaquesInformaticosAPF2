package com.utp.ataquesinformaticos.repository;

import com.utp.ataquesinformaticos.model.Amenaza;
import com.utp.ataquesinformaticos.model.NivelRiesgo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AmenazaRepository extends JpaRepository<Amenaza, Integer> {
    // Métodos básicos de CRUD ya están incluidos en JpaRepository
     // Obtener las últimas 10 amenazas por fecha de detección descendente
    List<Amenaza> findTop10ByOrderByFechaDeteccionDesc();

    //Obtener amenazas por nivel de riesgo (JPQL)
    @Query("SELECT a FROM Amenaza a WHERE a.nivelRiesgo = :nivel")
    List<Amenaza> buscarPorNivel(@Param("nivel") NivelRiesgo nivel);

    //Buscar amenazas por tipo, ignorando mayúsculas (JPQL)
    @Query("SELECT a FROM Amenaza a WHERE LOWER(a.tipo) LIKE LOWER(CONCAT('%', :tipo, '%'))")
    List<Amenaza> buscarPorTipo(@Param("tipo") String tipo);

    //Obtener amenazas ordenadas por ID descendente, paginadas (JPQL)
    @Query("SELECT a FROM Amenaza a ORDER BY a.id DESC")
    Page<Amenaza> obtenerTodasPaginadas(Pageable pageable);
    
     // Contar por nivel de riesgo
    int countByNivelRiesgo(NivelRiesgo nivelRiesgo);

    
    // Contar amenazas por tipo
    @Query("SELECT a.tipo, COUNT(a) FROM Amenaza a GROUP BY a.tipo")
    List<Object[]> countByTipo();
}
