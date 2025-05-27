package com.utp.ataquesinformaticos.controller;

import com.utp.ataquesinformaticos.model.*;
import com.utp.ataquesinformaticos.repository.RolRepository;
import com.utp.ataquesinformaticos.service.*;

import jakarta.servlet.http.HttpSession;
import java.util.HashSet;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

//ATAQUES INFORMATICOS
@Controller
public class RegistroController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private VerificationCodeService verificationCodeService;

    // Mapeo para mostrar el formulario de registro
    @GetMapping("/registro")
    public String mostrarRegistro(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "analista/login2"; // Asumiendo que el formulario está en esta vista
    }

    // Mapeo para procesar el registro
    @PostMapping("/registrar")
    public String iniciarRegistro(@RequestParam String nombre,
            @RequestParam String email,
            @RequestParam String password,
            RedirectAttributes redirectAttributes,
            HttpSession session) {
        try {
            if (usuarioService.existsByEmail(email)) {
                redirectAttributes.addFlashAttribute("error", "El email ya está registrado");
                return "redirect:/registro";
            }

            // Generar código de verificación
            String codigoVerificacion = verificationCodeService.generarCodigoVerificacion();

            // Almacenar datos temporales en sesión
            session.setAttribute("nombre", nombre);
            session.setAttribute("email", email);
            session.setAttribute("password", password);
            session.setAttribute("codigoVerificacion", codigoVerificacion);

            // Enviar código de verificación
            verificationCodeService.enviarCodigoVerificacion(email, codigoVerificacion);

            // Redirigir a página de verificación
            return "redirect:/verificar-codigo";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error",
                    "Error durante el registro: " + e.getMessage());
            return "redirect:/registro";
        }
    }

    // Nuevo método para mostrar página de verificación
    @GetMapping("/verificar-codigo")
    public String mostrarVerificacion() {
        return "cliente/verificar-codigo"; // Nueva vista de verificación
    }

    // Método para verificar el código
    @PostMapping("/verificar")
    public String verificarCodigo(@RequestParam String codigo,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        
        String codigoGuardado = (String) session.getAttribute("codigoVerificacion");

        if (codigo.equals(codigoGuardado)) {
            // Crear usuario
            Usuario nuevoUsuario = new Usuario();
            nuevoUsuario.setNombre((String) session.getAttribute("nombre"));
            nuevoUsuario.setEmail((String) session.getAttribute("email"));
            nuevoUsuario.setUsername((String) session.getAttribute("email"));
            nuevoUsuario.setPassword(passwordEncoder.encode((String) session.getAttribute("password")));
            nuevoUsuario.setEnabled(true);
            nuevoUsuario.setVerificado(true);

            // Asignar rol CLIENTE (código similar al anterior)
            Rol rolCliente = rolRepository.findByNombre("CLIENTE");
            if (rolCliente == null) {
                rolCliente = new Rol("CLIENTE");
                rolRepository.save(rolCliente);
            }

            Set<Rol> roles = new HashSet<>();
            roles.add(rolCliente);
            nuevoUsuario.setRoles(roles);

            // Guardar usuario
            usuarioService.save(nuevoUsuario);

            // Limpiar sesión
            session.removeAttribute("nombre");
            session.removeAttribute("email");
            session.removeAttribute("password");
            session.removeAttribute("codigoVerificacion");

            redirectAttributes.addFlashAttribute("registroExitoso",
                    "Registro exitoso. Por favor, inicia sesión.");
            return "redirect:/login";
        } else {
            redirectAttributes.addFlashAttribute("error", "Código de verificación incorrecto");
            return "redirect:/verificar-codigo";
        }
    }

}