package com.utp.ataquesinformaticos.controller;

import com.utp.ataquesinformaticos.model.*;
import com.utp.ataquesinformaticos.security.SecurityUserDetails;
import com.utp.ataquesinformaticos.service.*;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/cliente")
public class ClienteController {
    
    @Autowired
    private AtaqueService ataqueService;
    
    @Autowired
    private AmenazaService amenazaService;
    
    @GetMapping("/home")
    public String homeCliente(Authentication authentication, Model model) {
        // Verificar que el usuario tiene rol CLIENTE
        if (!authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_CLIENTE"))) {
            return "redirect:/login?error";
        }
        
        SecurityUserDetails userDetails = (SecurityUserDetails) authentication.getPrincipal();
        Usuario usuarioActual = userDetails.getUsuario();
        
        // OBTENER ESTADÍSTICAS PARA CLIENTE (SOLO LECTURA)
        int totalAtaques = (int) ataqueService.countAllAtaques();
        int totalAmenazas = (int) amenazaService.countAllAmenazas();
        
        // Obtener datos recientes
        List<Ataque> ataquesRecientes = ataqueService.findRecentAtaques();
        List<Amenaza> amenazasRecientes = amenazaService.findRecentAmenazas();
        
        // Contar por severidad/nivel de riesgo
        int ataquesCriticos = ataqueService.contarAtaquesPorSeveridad(Severidad.CRITICA);
        int ataquesBajos = ataqueService.contarAtaquesPorSeveridad(Severidad.BAJA);
        int ataquesAltos = ataqueService.contarAtaquesPorSeveridad(Severidad.ALTA);
        int ataquesModerados = ataqueService.contarAtaquesPorSeveridad(Severidad.MODERADA);
        
        int amenazasCriticas = amenazaService.contarAmenazasPorNivelRiesgo(NivelRiesgo.CRITICO);
        int amenazasAltas = amenazaService.contarAmenazasPorNivelRiesgo(NivelRiesgo.ALTO);
        int amenazasMedias = amenazaService.contarAmenazasPorNivelRiesgo(NivelRiesgo.MEDIO);
        int amenazasBajas = amenazaService.contarAmenazasPorNivelRiesgo(NivelRiesgo.BAJO);
        
        // Añadir atributos al modelo
        model.addAttribute("usuario", usuarioActual);
        model.addAttribute("totalAtaques", totalAtaques);
        model.addAttribute("totalAmenazas", totalAmenazas);
        model.addAttribute("ultimosAtaques", ataquesRecientes);
        model.addAttribute("ultimasAmenazas", amenazasRecientes);
        model.addAttribute("ataquesCriticos", ataquesCriticos);
        model.addAttribute("ataquesBajos", ataquesBajos);
        model.addAttribute("ataquesAltos", ataquesAltos);
        model.addAttribute("ataquesModerados", ataquesModerados);
        model.addAttribute("amenazasCriticas", amenazasCriticas);
        model.addAttribute("amenazasAltas", amenazasAltas);
        model.addAttribute("amenazasMedias", amenazasMedias);
        model.addAttribute("amenazasBajas", amenazasBajas);
        model.addAttribute("currentPage", "clientes");
        
        return "cliente/home";
    }
    
   
    
    @GetMapping("/contacto")
    public String contactosCliente(Authentication authentication, Model model) {
        // Verificar rol
        if (!authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_CLIENTE"))) {
            return "redirect:/login?error";
        }
        
        SecurityUserDetails userDetails = (SecurityUserDetails) authentication.getPrincipal();
        Usuario usuarioActual = userDetails.getUsuario();
        
        
        model.addAttribute("usuario", usuarioActual);
         model.addAttribute("currentPage", "contactos");

        return "cliente/contact";
    }
    
}
