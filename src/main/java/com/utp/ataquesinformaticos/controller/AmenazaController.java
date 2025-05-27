package com.utp.ataquesinformaticos.controller;

import com.utp.ataquesinformaticos.dto.AmenazaDTO;
import org.springframework.stereotype.Controller;
import com.utp.ataquesinformaticos.model.*;
import com.utp.ataquesinformaticos.security.SecurityUserDetails;
import com.utp.ataquesinformaticos.service.*;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/amenazas")
public class AmenazaController {

    private final AmenazaService amenazaService;

    @Autowired
    public AmenazaController(AmenazaService amenazaService) {
        this.amenazaService = amenazaService;
    }

    @GetMapping
    public String listarAmenazas(Model model, Authentication authentication) {

        SecurityUserDetails userDetails = (SecurityUserDetails) authentication.getPrincipal();
        Usuario usuarioActual = userDetails.getUsuario();
        // Validar rol
        if (!tienePermisoAmenazas(authentication)) {
            return "redirect:/login?error";
        }

        List<AmenazaDTO> amenazas = amenazaService.listarTodasLasAmenazas();
        // Añadir el usuario al modelo
        model.addAttribute("usuario", usuarioActual);

        model.addAttribute("amenazas", amenazas);
        model.addAttribute("currentPage", "amenazas");
        model.addAttribute("nivelRiesgoValues", NivelRiesgo.values());

        return "analista/amenazas/lista";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model, Authentication authentication) {
        SecurityUserDetails userDetails = (SecurityUserDetails) authentication.getPrincipal();
        Usuario usuarioActual = userDetails.getUsuario();
        if (!tienePermisoAmenazas(authentication)) {
            return "redirect:/login?error";
        }
        // Añadir el usuario al modelo
        model.addAttribute("usuario", usuarioActual);
        model.addAttribute("amenaza", new AmenazaDTO());
        model.addAttribute("nivelRiesgoValues", NivelRiesgo.values());
        return "analista/amenazas/formulario";
    }

    // Corrección en AmenazaController para el método guardarAmenaza
    @PostMapping("/guardar")
    public String guardarAmenaza(@ModelAttribute AmenazaDTO amenazaDTO,
            Authentication authentication) {
        if (!tienePermisoAmenazas(authentication)) {
            return "redirect:/login?error";
        }

        // Verificar si es una actualización o una nueva amenaza
        if (amenazaDTO.getId() != null) {
            amenazaService.actualizarAmenaza(amenazaDTO.getId(), amenazaDTO);
        } else {
            amenazaService.guardarAmenaza(amenazaDTO);
        }
        return "redirect:/amenazas";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Integer id, Model model,
            Authentication authentication) {
        SecurityUserDetails userDetails = (SecurityUserDetails) authentication.getPrincipal();
        Usuario usuarioActual = userDetails.getUsuario();
        if (!tienePermisoAmenazas(authentication)) {
            return "redirect:/login?error";
        }

        AmenazaDTO amenaza = amenazaService.buscarPorId(id);
        // Añadir el usuario al modelo
        model.addAttribute("usuario", usuarioActual);
        model.addAttribute("amenaza", amenaza);
        model.addAttribute("nivelRiesgoValues", NivelRiesgo.values());
        return "analista/amenazas/formulario";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarAmenaza(@PathVariable Integer id, Authentication authentication) {
        if (!tienePermisoAmenazas(authentication)) {
            return "redirect:/login?error";
        }

        amenazaService.eliminarAmenaza(id);
        return "redirect:/amenazas";
    }
    
     @GetMapping("/detalle/{id}")
    public String detalleAmenaza(@PathVariable Integer id, Model model, Authentication authentication) {

        SecurityUserDetails userDetails = (SecurityUserDetails) authentication.getPrincipal();
        Usuario usuarioActual = userDetails.getUsuario();
        Amenaza amenaza = amenazaService.findById(id);

        if (amenaza == null) {
            return "redirect:/analista/amenazas?error=NotFound";
        }
        //Añadir el usuario al modelo
        model.addAttribute("usuario", usuarioActual);

        model.addAttribute("amenaza", amenaza);
        return "analista/detalle-amenaza";
    }

    private boolean tienePermisoAmenazas(Authentication authentication) {
        return authentication != null
                && authentication.getAuthorities().stream()
                        .anyMatch(a -> a.getAuthority().equals("ROLE_ANALISTA")
                        || a.getAuthority().equals("ROLE_ADMINISTRADOR") || a.getAuthority().equals("ROLE_CLIENTE")
                        );
    }
}
