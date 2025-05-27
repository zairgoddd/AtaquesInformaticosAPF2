package com.utp.ataquesinformaticos.controller;

import java.util.Collection;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    // Muestra la página de login
    @GetMapping("/login")
    public String mostrarLogin(Model model) {
        return "analista/login2";
    }
    
     // Maneja la redirección después del login exitoso
    @GetMapping("/redirect")
    public String redirectBasedOnRole(Authentication authentication) {
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        
        // Verificar si el usuario tiene rol de ANALISTA
        if (authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_ANALISTA"))) {
            return "redirect:/analista/home";
        }
        // Verificar si el usuario tiene rol de CLIENTE
        else if (authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_CLIENTE"))) {
            return "redirect:/cliente/home";
        }
          // Verificar si el usuario tiene rol de ADMINISTRADOR TI
         else if (authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMINISTRADOR"))) {
            return "redirect:/administrador/home";
        }
        
        return "redirect:/login?error";
    }
}