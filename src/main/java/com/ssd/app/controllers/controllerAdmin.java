package com.ssd.app.controllers;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import org.owasp.encoder.Encode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;

import com.ssd.app.model.dtoModificaDettaglio;
import com.ssd.app.model.dtoSpesaConMatricola;
import com.ssd.app.model.modifica;
import com.ssd.app.model.spesa;
import com.ssd.app.repository.modificaRepo;
import com.ssd.app.repository.speseRepo;

import lombok.extern.apachecommons.CommonsLog;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@CommonsLog
public class controllerAdmin {
    
    private ModelAndView mv;


    @Autowired
    private speseRepo speseRepo;

    @Autowired
    private modificaRepo modificaRepo;

    public controllerAdmin(){
        mv = new ModelAndView();
    }

    @GetMapping(value="/adminhome")
    public ModelAndView getAdminHome() {
        mv.setViewName("adminhome");
        return mv;
    }
    
    @GetMapping(value="/listaSpeseAdmin")
    public ModelAndView getListaSpeseAdmin() {
        return componiListaSpese(false);     
    }
 
    @PostMapping(value="cancellaSpesa/{id}")
    public ModelAndView rimuoviSpesa(@PathVariable("id") Long id_spesa,Principal principal) {
        if(modificaRepo.existsByVecchiaSpesa(speseRepo.getReferenceById(id_spesa))){
            return componiListaSpese(true);
        }
        speseRepo.delete(speseRepo.getReferenceById(id_spesa));
        log.warn("[ADMIN " + principal.getName() + "] SPESA ID : "+ id_spesa + " Cancellata");
        return getListaSpeseAdmin();
    }
    
    @GetMapping(value="/listaModificheAdmin")
    public ModelAndView getListaModifiche() {
        
        return componiListaModifiche(false);
    }
    
    @PostMapping(value="/accettaModifica/{id}")
    public ModelAndView accettaModifica(@PathVariable("id") Long id_modifica,Principal principal) {
        modifica modifica = modificaRepo.getReferenceById(id_modifica);
        
        if(modifica.getVecchiaSpesa().getMatricola().compareTo(principal.getName())==0){
            log.warn("[ADMIN " + principal.getName() + "] CONFLITTO TENTATA MODIFICA ID: " + id_modifica +" ESITO : Bloccata");
            return componiListaModifiche(true);
        }

        spesa spesa_aggiornata = new spesa(
            modifica.getVecchiaSpesa().getId(), 
            modifica.getVecchiaSpesa().getMatricola(), 
            modifica.getNuovoTotale(),
            modifica.getNuovaData(),
            modifica.getNuovaDescrizione());

        modificaRepo.delete(modifica);
        speseRepo.delete(modifica.getVecchiaSpesa());
        speseRepo.save(spesa_aggiornata);

        log.warn("[ADMIN " + principal.getName() + "] PROCESSATA MODIFICA ID: " + id_modifica +" ESITO : Accettata");
        return getListaModifiche();
    }

    @PostMapping(value="rifiutaModifica/{id}")
    public ModelAndView rifiutaModifica(@PathVariable("id") Long id_modifica,Principal principal) {
        modifica modifica = modificaRepo.getReferenceById(id_modifica);

        if(modifica.getVecchiaSpesa().getMatricola().compareTo(principal.getName())==0){
            log.warn("[ADMIN " + principal.getName() + "] CONFLITTO TENTATA CANCELLAZIONE ID: " + id_modifica +" ESITO : Bloccata");
            return componiListaModifiche(true);
        }

        modificaRepo.delete(modificaRepo.getReferenceById(id_modifica));
        log.warn("[ADMIN " + principal.getName() + "] PROCESSATA MODIFICA ID: " + id_modifica +" ESITO : Rifiutata");
        return getListaModifiche();
    }

    private ModelAndView componiListaSpese(boolean errore){
        List<spesa> allspese = speseRepo.findAll();
        List<dtoSpesaConMatricola> allspesematricola = new ArrayList<>();
        Integer contatoreSpese = 0;
        Float Saldo = 0F;

        for (spesa spesa : allspese) {
            dtoSpesaConMatricola dto = new dtoSpesaConMatricola(spesa.getId(), spesa.getTotale(), spesa.getData(), spesa.getMatricola(), Encode.forHtml(spesa.getDescription()));
            allspesematricola.add(dto);
            contatoreSpese++;
            Saldo=Saldo+spesa.getTotale();
        }
        mv.addObject("error_modifica_pending",errore);
        mv.addObject("contatore",contatoreSpese);
        mv.addObject("saldo", Saldo);
        mv.addObject("lista_spese", allspesematricola);
        mv.setViewName("listaSpeseAdmin");
        return mv;  
    }

    private ModelAndView componiListaModifiche(boolean error){
        List<modifica> lista_modifiche = new ArrayList<>(modificaRepo.findAll());
        List<dtoModificaDettaglio> lista_modifiche_dto= new ArrayList<>();
        for (modifica modifica : lista_modifiche) {
            dtoModificaDettaglio dto = new dtoModificaDettaglio(
                                            modifica.getId(), 
                                            modifica.getNuovoTotale(), 
                                            modifica.getNuovaData(), 
                                            Encode.forHtml(modifica.getNuovaDescrizione())  , 
                                            modifica.getVecchiaSpesa().getId(), 
                                            modifica.getVecchiaSpesa().getTotale(), 
                                            modifica.getVecchiaSpesa().getData(), 
                                            Encode.forHtml(modifica.getVecchiaSpesa().getDescription()) );
            lista_modifiche_dto.add(dto);
        }

        mv.addObject("errore_conflitto_modifica",error);
        mv.addObject("lista_modifiche", lista_modifiche_dto);
        mv.setViewName("listaModificheAdmin");
        return mv;
    }
}
