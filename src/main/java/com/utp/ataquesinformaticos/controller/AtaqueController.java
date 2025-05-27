package com.utp.ataquesinformaticos.controller;

import com.utp.ataquesinformaticos.dto.AmenazaDTO;
import com.utp.ataquesinformaticos.dto.AtaqueDTO;
import com.utp.ataquesinformaticos.model.Ataque;
import com.utp.ataquesinformaticos.model.Severidad;
import com.utp.ataquesinformaticos.model.Usuario;
import com.utp.ataquesinformaticos.security.SecurityUserDetails;
import com.utp.ataquesinformaticos.service.AmenazaService;
import com.utp.ataquesinformaticos.service.AtaqueService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/ataques")
public class AtaqueController {

    private final AtaqueService ataqueService;
    private final AmenazaService amenazaService;

    @Autowired
    public AtaqueController(AtaqueService ataqueService, AmenazaService amenazaService) {
        this.ataqueService = ataqueService;
        this.amenazaService = amenazaService;
    }

    @GetMapping
    public String listarAtaques(Model model, Authentication authentication) {
        SecurityUserDetails userDetails = (SecurityUserDetails) authentication.getPrincipal();
        Usuario usuarioActual = userDetails.getUsuario();
        // Validar rol
        if (!tienePermisoAtaques(authentication)) {
            return "redirect:/login?error";
        }

        List<AtaqueDTO> ataques = ataqueService.listarTodosLosAtaques();
        // Añadir el usuario al modelo
        model.addAttribute("usuario", usuarioActual);
        model.addAttribute("ataques", ataques);
        model.addAttribute("currentPage", "ataques");

        return "analista/ataques/lista";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model, Authentication authentication) {
        SecurityUserDetails userDetails = (SecurityUserDetails) authentication.getPrincipal();
        Usuario usuarioActual = userDetails.getUsuario();
        if (!tienePermisoAtaques(authentication)) {
            return "redirect:/login?error";
        }
        // Añadir el usuario al modelo
        model.addAttribute("usuario", usuarioActual);

        model.addAttribute("ataque", new AtaqueDTO());
        model.addAttribute("amenazas", amenazaService.listarTodasLasAmenazas());
        model.addAttribute("severidadValues", Severidad.values());
        return "analista/ataques/formulario";
    }

    // Corrección en AtaqueController para el método guardarAtaque
    @PostMapping("/guardar")
    public String guardarAtaque(@ModelAttribute AtaqueDTO ataqueDTO,
            Authentication authentication) {
        if (!tienePermisoAtaques(authentication)) {
            return "redirect:/login?error";
        }

        // Verificar si es una actualización o un nuevo ataque
        if (ataqueDTO.getId() != null) {
            ataqueService.actualizarAtaque(ataqueDTO.getId(), ataqueDTO);
        } else {
            ataqueService.guardarAtaque(ataqueDTO);
        }
        return "redirect:/ataques";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Integer id, Model model,
            Authentication authentication) {
        SecurityUserDetails userDetails = (SecurityUserDetails) authentication.getPrincipal();
        Usuario usuarioActual = userDetails.getUsuario();
        if (!tienePermisoAtaques(authentication)) {
            return "redirect:/login?error";
        }

        AtaqueDTO ataque = ataqueService.buscarPorId(id);
        // Añadir el usuario al modelo
        model.addAttribute("usuario", usuarioActual);
        model.addAttribute("ataque", ataque);
        model.addAttribute("amenazas", amenazaService.listarTodasLasAmenazas());
        model.addAttribute("severidadValues", Severidad.values());
        return "analista/ataques/formulario";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarAtaque(@PathVariable Integer id, Authentication authentication) {
        if (!tienePermisoAtaques(authentication)) {
            return "redirect:/login?error";
        }

        ataqueService.eliminarAtaque(id);
        return "redirect:/ataques";
    }

    @GetMapping("/amenaza/{amenazaId}")
    public String listarAtaquesPorAmenaza(@PathVariable Integer amenazaId, Model model,
            Authentication authentication) {
        if (!tienePermisoAtaques(authentication)) {
            return "redirect:/login?error";
        }

        List<AtaqueDTO> ataques = ataqueService.buscarPorAmenazaId(amenazaId);
        AmenazaDTO amenaza = amenazaService.buscarPorId(amenazaId);

        model.addAttribute("ataques", ataques);
        model.addAttribute("amenaza", amenaza);

        return "analista/ataques/listaPorAmenaza";
    }
    
     @GetMapping("/detalle/{id}")
    public String detalleAtaque(@PathVariable Integer id, Model model, Authentication authentication) {
        
        SecurityUserDetails userDetails = (SecurityUserDetails) authentication.getPrincipal();
        Usuario usuarioActual = userDetails.getUsuario();
        Ataque ataque = ataqueService.findById(id);

        if (ataque == null) {
            return "redirect:/analista/ataques?error=NotFound";
        }

        //Añadir el usuario al modelo
        model.addAttribute("usuario", usuarioActual);
        model.addAttribute("ataque", ataque);

        return "analista/detalle-ataque";
    }

    private boolean tienePermisoAtaques(Authentication authentication) {
        return authentication != null
                && authentication.getAuthorities().stream()
                        .anyMatch(a -> a.getAuthority().equals("ROLE_ANALISTA")
                        || a.getAuthority().equals("ROLE_ADMINISTRADOR")
                        || a.getAuthority().equals("ROLE_CLIENTE")
                        );
    }
}
