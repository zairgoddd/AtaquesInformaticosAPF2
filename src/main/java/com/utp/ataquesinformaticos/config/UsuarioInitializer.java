package com.utp.ataquesinformaticos.config;

import com.utp.ataquesinformaticos.model.*;
import com.utp.ataquesinformaticos.repository.*;
import jakarta.annotation.PostConstruct;
import java.util.HashSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UsuarioInitializer {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RolRepository rolRepository;

    @PostConstruct
    public void init() {

        // Crear roles si no existen
        //Creando Rol 1 --> Analista de Seguridad 
        Rol rolAnalista = rolRepository.findByNombre("ANALISTA");
        if (rolAnalista == null) {
            rolAnalista = new Rol("ANALISTA");
            rolRepository.save(rolAnalista);
        }

        //Creando Rol 2 --> Administrador TI
        Rol rolAdministrador = rolRepository.findByNombre("ADMINISTRADOR");
        if (rolAdministrador == null) {
            rolAdministrador = new Rol("ADMINISTRADOR");
            rolRepository.save(rolAdministrador);
        }

        //Creando Rol 3 --> Cliente
        // Añadir rol CLIENTE
        Rol rolCliente = rolRepository.findByNombre("CLIENTE");
        if (rolCliente == null) {
            rolCliente = new Rol("CLIENTE");
            rolRepository.save(rolCliente);
        }

        // Crear usuario ANALISTA si no existe
        if (!usuarioRepository.existsByUsername("analista")) {
            Usuario analista = new Usuario();
            analista.setUsername("analista");
            analista.setPassword(passwordEncoder.encode("analista12345"));
            analista.setEmail("analista31@gmail.com");
            analista.setNombre("Analista Martin");
            analista.setEnabled(true);
            analista.setRoles(new HashSet<>());
            analista.getRoles().add(rolAnalista);

            usuarioRepository.save(analista);
        }

        // Crear usuario ADMINISTRADOR si no existe
        if (!usuarioRepository.existsByUsername("admin")) {
            Usuario admin = new Usuario();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setEmail("administradorTI@gmail.com");
            admin.setNombre("Administrador Lucas");
            admin.setEnabled(true);
            admin.setRoles(new HashSet<>());
            admin.getRoles().add(rolAdministrador);

            usuarioRepository.save(admin);
        }

    }

}
