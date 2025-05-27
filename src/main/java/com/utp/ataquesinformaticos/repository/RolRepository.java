package com.utp.ataquesinformaticos.repository;

import com.utp.ataquesinformaticos.model.Rol;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RolRepository extends JpaRepository<Rol, Integer> {

    Rol findByNombre(String nombre);
     boolean existsByNombre(String nombre);
}
