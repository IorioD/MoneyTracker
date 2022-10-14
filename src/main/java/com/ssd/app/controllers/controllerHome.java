package com.ssd.app.controllers;

import java.security.Principal;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import lombok.extern.apachecommons.CommonsLog;

@Controller
@CommonsLog
public class controllerHome{
    
    private ModelAndView mv;

    public controllerHome(){
        mv = new ModelAndView();
    }

    @GetMapping({"/",""})
    public ModelAndView index(){
        mv.setViewName("index");
        return mv;
    }

    @GetMapping("/home")
    public ModelAndView getHomepage(Principal Principal){
        mv.setViewName("userhome");  
        mv.addObject("nome",Principal.getName());
        return mv;
    }

    @GetMapping("/access_denied")
    public ModelAndView getAccessDeniedPage(Principal principal){
        mv.setViewName("accessdenied");
        log.warn("[HOME "+principal.getName()+"]RILEVATO TENTATIVO DI ACCESSO NON AUTORIZZATO");
        return mv;
    }

    @GetMapping("/error")
    public ModelAndView getErrorpage(Principal principal){
        mv.setViewName("error");
        log.warn("[HOME "+principal.getName()+"] RILEVATO ERRORE DI SISTEMA");
        return mv;
    }

    @GetMapping("/logout")
    public ModelAndView logout(HttpServletRequest request) throws ServletException{
        request.logout();
        mv.setViewName("index");
        return mv;
    }
}
