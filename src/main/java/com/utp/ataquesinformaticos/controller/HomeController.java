package com.utp.ataquesinformaticos.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/ataquesinformaticos")
public class HomeController {

    @GetMapping("")
    public String home(Model model) {
 
        return "analista/login2";
    }

}
