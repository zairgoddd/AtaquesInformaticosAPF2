package com.utp.ataquesinformaticos.controller;

import com.utp.ataquesinformaticos.model.*;
import com.utp.ataquesinformaticos.security.SecurityUserDetails;
import com.utp.ataquesinformaticos.service.*;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/administrador")
public class AdministradorController {

    @Autowired
    private AtaqueService ataqueService;

    @Autowired
    private AmenazaService amenazaService;

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/home")
    public String homeAdmin(Authentication authentication, Model model) {
        // Verificar que el usuario tiene rol ADMIN
        if (!authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMINISTRADOR"))) {
            return "redirect:/login?error";
        }

        SecurityUserDetails userDetails = (SecurityUserDetails) authentication.getPrincipal();
        Usuario usuarioActual = userDetails.getUsuario();

        // OBTENER ESTADÍSTICAS
        int totalAtaques = (int) ataqueService.countAllAtaques();
        int totalAmenazas = (int) amenazaService.countAllAmenazas();

        // Obtener ataques y amenazas recientes
        List<Ataque> ataquesRecientes = ataqueService.findRecentAtaques();
        List<Amenaza> amenazasRecientes = amenazaService.findRecentAmenazas();

        // Contar ataques por severidad
        int ataquesCriticos = ataqueService.contarAtaquesPorSeveridad(Severidad.CRITICA);
        int ataquesBajos = ataqueService.contarAtaquesPorSeveridad(Severidad.BAJA);

        // Añadir atributos al modelo
        model.addAttribute("usuario", usuarioActual);
        model.addAttribute("totalAtaques", totalAtaques);
        model.addAttribute("totalAmenazas", totalAmenazas);
        model.addAttribute("ultimosAtaques", ataquesRecientes);
        model.addAttribute("ultimasAmenazas", amenazasRecientes);
        model.addAttribute("ataquesCriticos", ataquesCriticos);
        model.addAttribute("ataquesBajos", ataquesBajos);

        return "administrador/home1";

    }

    @GetMapping("/usuarios")
    public String gestionarUsuarios(Authentication authentication, Model model) {
        // Verificar rol
        if (!authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMINISTRADOR"))) {
            return "redirect:/login?error";
        }

        SecurityUserDetails userDetails = (SecurityUserDetails) authentication.getPrincipal();
        Usuario usuarioActual = userDetails.getUsuario();

        // Obtener todos los usuarios y roles
        List<Usuario> usuarios = usuarioService.findAllUsuarios();
        List<Rol> roles = usuarioService.findAllRoles();

        model.addAttribute("usuario", usuarioActual);
        model.addAttribute("usuarios", usuarios);
        model.addAttribute("roles", roles);

        return "administrador/usuarios3";
    }

    @GetMapping("/configuracion")
    public String configuracion(Authentication authentication, Model model) {
        // Verificar rol
        if (!authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMINISTRADOR"))) {
            return "redirect:/login?error";
        }

        SecurityUserDetails userDetails = (SecurityUserDetails) authentication.getPrincipal();
        Usuario usuarioActual = userDetails.getUsuario();

        model.addAttribute("usuario", usuarioActual);

        return "administrador/configuracion";
    }

    @PostMapping("/usuarios/{id}/toggle-estado")
    public String toggleEstadoUsuario(@PathVariable Integer id, Authentication authentication) {
        // Verificar rol
        if (!authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMINISTRADOR"))) {
            return "redirect:/login?error";
        }

        try {
            usuarioService.toggleEstadoUsuario(id);
            return "redirect:/administrador/usuarios?success=estado_actualizado";
        } catch (Exception e) {
            return "redirect:/administrador/usuarios?error=no_se_pudo_actualizar";
        }
    }

    @PostMapping("/usuarios/{id}/cambiar-rol")
    public String cambiarRolUsuario(@PathVariable Integer id,
            @RequestParam String nuevoRol,
            Authentication authentication) {
        // Verificar rol
        if (!authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMINISTRADOR"))) {
            return "redirect:/login?error";
        }

        try {
            usuarioService.cambiarRolUsuario(id, nuevoRol);
            return "redirect:/administrador/usuarios?success=rol_actualizado";
        } catch (Exception e) {
            return "redirect:/administrador/usuarios?error=no_se_pudo_cambiar_rol";
        }
    }

}
