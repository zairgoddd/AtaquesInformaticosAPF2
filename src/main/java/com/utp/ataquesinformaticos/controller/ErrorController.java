package com.utp.ataquesinformaticos.controller;

import com.utp.ataquesinformaticos.model.Usuario;
import com.utp.ataquesinformaticos.security.SecurityUserDetails;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ErrorController implements org.springframework.boot.web.servlet.error.ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model, Authentication authentication) {
        
         SecurityUserDetails userDetails = (SecurityUserDetails) authentication.getPrincipal();
        Usuario usuarioActual = userDetails.getUsuario();
        
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        String errorMessage = "Ha ocurrido un error";
        String errorDetails = "";
        
        if (status != null) {
            Integer statusCode = Integer.valueOf(status.toString());
            
            if(statusCode == HttpStatus.NOT_FOUND.value()) {
                errorMessage = "Página no encontrada";
                errorDetails = "La página que busca no existe";
            }
            else if(statusCode == HttpStatus.FORBIDDEN.value()) {
                errorMessage = "Acceso denegado";
                errorDetails = "No tiene permisos para acceder a esta página!";
            }
            else if(statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                errorMessage = "Error interno";
                errorDetails = "Ha ocurrido un error interno en el servidor";
            }
        }
        
        // Añadir el usuario al modelo
        model.addAttribute("usuario", usuarioActual);
        model.addAttribute("errorMessage", errorMessage);
        model.addAttribute("errorDetails", errorDetails);
        
        return "error";
    }
}
