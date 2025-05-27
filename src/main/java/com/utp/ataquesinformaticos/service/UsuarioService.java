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
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public Optional<Usuario> findById(Integer id) {
        return usuarioRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Usuario> findAllUsuarios() {
        return usuarioRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Usuario findByUsername(String username) {
        return usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con username: " + username));
    }

    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return usuarioRepository.existsByEmail(email);
    }

    @Transactional(readOnly = true)
    public Optional<Usuario> findByEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    @Transactional
    public Usuario save(Usuario usuario) {
        if (usuarioRepository.findByUsername(usuario.getUsername()).isPresent()) {
            throw new IllegalArgumentException("El nombre de usuario ya existe");
        }

        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            throw new IllegalArgumentException("El email ya está registrado");
        }

        if (usuario.getUsername() == null || usuario.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de usuario no puede estar vacío");
        }

        if (usuario.getPassword() == null || usuario.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("La contraseña no puede estar vacía");
        }

        if (usuario.getEmail() == null || usuario.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("El email no puede estar vacío");
        }

        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        if (!usuario.getEmail().matches(emailRegex)) {
            throw new IllegalArgumentException("Formato de email inválido");
        }

        return usuarioRepository.save(usuario);
    }

    @Transactional(readOnly = true)
    public List<Usuario> findAllClientUsers() {
        return usuarioRepository.findByRoles_Nombre("CLIENTE");
    }

    @Transactional(readOnly = true)
    public Page<Usuario> findAllClientUsers(Pageable pageable) {
        return usuarioRepository.findByRoles_Nombre("CLIENTE", pageable);
    }

    @Transactional(readOnly = true)
    public boolean verificarContraseñaActual(Usuario usuario, String contraseñaActual) {
        return passwordEncoder.matches(contraseñaActual, usuario.getPassword());
    }

    @Transactional
    public Usuario actualizarUsuario(Usuario usuario) {
        Optional<Usuario> existingUser = usuarioRepository.findByUsername(usuario.getUsername());
        if (existingUser.isPresent() && !existingUser.get().getId().equals(usuario.getId())) {
            throw new IllegalArgumentException("El nombre de usuario ya existe");
        }

        Optional<Usuario> existingEmail = usuarioRepository.findByEmail(usuario.getEmail());
        if (existingEmail.isPresent() && !existingEmail.get().getId().equals(usuario.getId())) {
            throw new IllegalArgumentException("El email ya está registrado");
        }

        if (usuario.getUsername() == null || usuario.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de usuario no puede estar vacío");
        }

        if (usuario.getEmail() == null || usuario.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("El email no puede estar vacío");
        }

        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        if (!usuario.getEmail().matches(emailRegex)) {
            throw new IllegalArgumentException("Formato de email inválido");
        }

        return usuarioRepository.save(usuario);
    }

    @Transactional
    public void toggleEstadoUsuario(Integer id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        usuario.setEnabled(!usuario.isEnabled());
        usuarioRepository.save(usuario);
    }

    @Transactional
    public void cambiarRolUsuario(Integer id, String nuevoRol) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Rol rol = rolRepository.findByNombre(nuevoRol);
        if (rol == null) {
            throw new RuntimeException("Rol no encontrado: " + nuevoRol);
        }

        usuario.getRoles().clear();
        usuario.getRoles().add(rol);

        usuarioRepository.save(usuario);
    }

    @Transactional(readOnly = true)
    public List<Rol> findAllRoles() {
        return rolRepository.findAll();
    }
}
