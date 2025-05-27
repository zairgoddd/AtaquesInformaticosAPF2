package com.utp.ataquesinformaticos.service;

import com.utp.ataquesinformaticos.model.*;
import com.utp.ataquesinformaticos.repository.*;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Método para obtener un asunto por ID
    public Optional<Usuario> findById(Integer id) {
        return usuarioRepository.findById(id);
    }

    //Obtiene todos los usuarios del sistema
    public List<Usuario> findAllUsuarios() {
        return usuarioRepository.findAll();
    }

    public Usuario findByUsername(String username) {
        return usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con username: " + username));
    }

    public boolean existsByEmail(String email) {
        return usuarioRepository.existsByEmail(email);
    }

    public Optional<Usuario> findByEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    public Usuario save(Usuario usuario) {
        // Verificar si el username ya existe
        if (usuarioRepository.findByUsername(usuario.getUsername()).isPresent()) {
            throw new IllegalArgumentException("El nombre de usuario ya existe");
        }

        // Verificar si el email ya existe
        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            throw new IllegalArgumentException("El email ya está registrado");
        }

        // Validaciones básicas
        if (usuario.getUsername() == null || usuario.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de usuario no puede estar vacío");
        }

        if (usuario.getPassword() == null || usuario.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("La contraseña no puede estar vacía");
        }

        if (usuario.getEmail() == null || usuario.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("El email no puede estar vacío");
        }

        // Validar formato de email usando una expresión regular simple
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        if (!usuario.getEmail().matches(emailRegex)) {
            throw new IllegalArgumentException("Formato de email inválido");
        }

        return usuarioRepository.save(usuario);
    }

    public List<Usuario> findAllClientUsers() {
        return usuarioRepository.findByRoles_Nombre("CLIENTE");
    }

    public Page<Usuario> findAllClientUsers(Pageable pageable) {
        return usuarioRepository.findByRoles_Nombre("CLIENTE", pageable);
    }

    public boolean verificarContraseñaActual(Usuario usuario, String contraseñaActual) {
        return passwordEncoder.matches(contraseñaActual, usuario.getPassword());
    }

    public Usuario actualizarUsuario(Usuario usuario) {
        // Verificar si existe otro usuario con el mismo username (excluyendo el usuario actual)
        Optional<Usuario> existingUser = usuarioRepository.findByUsername(usuario.getUsername());
        if (existingUser.isPresent() && !existingUser.get().getId().equals(usuario.getId())) {
            throw new IllegalArgumentException("El nombre de usuario ya existe");
        }

        // Verificar si existe otro usuario con el mismo email (excluyendo el usuario actual)
        Optional<Usuario> existingEmail = usuarioRepository.findByEmail(usuario.getEmail());
        if (existingEmail.isPresent() && !existingEmail.get().getId().equals(usuario.getId())) {
            throw new IllegalArgumentException("El email ya está registrado");
        }

        // Validaciones básicas
        if (usuario.getUsername() == null || usuario.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de usuario no puede estar vacío");
        }

        if (usuario.getEmail() == null || usuario.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("El email no puede estar vacío");
        }

        // Validar formato de email
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        if (!usuario.getEmail().matches(emailRegex)) {
            throw new IllegalArgumentException("Formato de email inválido");
        }

        return usuarioRepository.save(usuario);
    }

    //Cambia el estado (enabled/disabled) de un usuario
    public void toggleEstadoUsuario(Integer id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        usuario.setEnabled(!usuario.isEnabled());
        usuarioRepository.save(usuario);
    }

    //Cambiar el rol de un usuario
    public void cambiarRolUsuario(Integer id, String nuevoRol) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Rol rol = rolRepository.findByNombre(nuevoRol);
        if (rol == null) {
            throw new RuntimeException("Rol no encontrado: " + nuevoRol);
        }

        // Limpiar roles actuales y asignar el nuevo
        usuario.getRoles().clear();
        usuario.getRoles().add(rol);

        usuarioRepository.save(usuario);
    }

    //Obtiene todos los roles disponibles
    public List<Rol> findAllRoles() {
        return rolRepository.findAll();
    }

}
