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
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/analista")
public class AnalistaController {

    @Autowired
    private AtaqueService ataqueService;

    @Autowired
    private AmenazaService amenazaService;

    @GetMapping("/home")
    public String homeAdmin(Authentication authentication, Model model) {
        // Verificar que el usuario tiene rol ADMIN
        if (!authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ANALISTA"))) {
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

        return "analista/home1";

    }

}
