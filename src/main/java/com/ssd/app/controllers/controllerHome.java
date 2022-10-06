package com.ssd.app.controllers;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import com.ssd.app.model.utente;
import com.ssd.app.repository.utenteRepo;

@Controller
public class controllerHome{
    
    private ModelAndView mv;
    
    @Autowired
    private utenteRepo utenteRepo;

    public controllerHome(){
        mv = new ModelAndView();
    }

    @GetMapping({"/","/logout_success"})
    public ModelAndView index(){
        mv.setViewName("index");
        return mv;
    }

    @GetMapping("/login")
    public ModelAndView getlogin(){
        mv.setViewName("login");
        return mv;
    }

    @GetMapping("/home")
    public ModelAndView getHomepage(Principal Principal){
        utente utenteAuth = utenteRepo.findByMatricola(Principal.getName());
        mv.setViewName("userhome");  
        mv.addObject("nome",utenteAuth.getNome());
        return mv;
    }

    @GetMapping("/access_denied")
    public ModelAndView getAccessDeniedPage(){
        mv.setViewName("accessdenied");
        return mv;
    }

    @GetMapping("/error")
    public ModelAndView getErrorpage(){
        mv.setViewName("error");
        return mv;
    }
}
