package com.ssd.app.controllers;

import java.util.ArrayList;
import java.util.List;

import org.owasp.encoder.Encode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;

import com.ssd.app.model.dtoModificaDettaglio;
import com.ssd.app.model.dtoSpesaConMatricola;
import com.ssd.app.model.modifica;
import com.ssd.app.model.spesa;
import com.ssd.app.model.utente;
import com.ssd.app.repository.modificaRepo;
import com.ssd.app.repository.speseRepo;
import com.ssd.app.repository.utenteRepo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;




@Controller
public class controllerAdmin {
    
    private ModelAndView mv;


    @Autowired
    private speseRepo speseRepo;

    @Autowired
    private utenteRepo utenteRepo;

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
        return ComponiListaSpese(false);     
    }
    
    @GetMapping(value="/listaUtentiAdmin")
    public  ModelAndView getlistaUtentiAdmin() {
        mv.addObject("lista_utenti", utenteRepo.findAll());
        mv.setViewName("listaUtentiAdmin");
        return mv;
    }
    
    @GetMapping(value="/formAddUtente")
    public ModelAndView getFormAddUtente() {
        mv.addObject("utente", new utente());
        mv.setViewName("formAddUtente");
        return mv;
    }

    @PostMapping(value="/saveUtente")
    public ModelAndView saveUtente(@ModelAttribute("utente") utente utente) {
        utente encodedUtente = new utente();
        encodedUtente.setNome(utente.getNome());
        encodedUtente.setCognome(utente.getCognome());
        encodedUtente.setMatricola(utente.getMatricola());
        encodedUtente.setRuolo(utente.getRuolo());
        
        encodedUtente.setPin(new BCryptPasswordEncoder().encode(utente.getPin()));

        utenteRepo.save(encodedUtente);
        
        return getlistaUtentiAdmin();
}

    @GetMapping(value="/formUpdateUtente/{id}")
    public ModelAndView getFormUpdateUtente(@PathVariable("id") Long id_utente) {
        mv.addObject("utente", utenteRepo.findById(id_utente));
        mv.setViewName("formUpdateUtente");
        return mv;
    }
    
    @PostMapping(value="/updateUtente")
    public ModelAndView updateUtente(@ModelAttribute("utente") utente utente) {
        utente encodedUtente = new utente();
        encodedUtente.setNome(utente.getNome());
        encodedUtente.setCognome(utente.getCognome());
        encodedUtente.setMatricola(utente.getMatricola());
        encodedUtente.setRuolo(utente.getRuolo());
        
        encodedUtente.setPin(new BCryptPasswordEncoder().encode(utente.getPin()));

        utenteRepo.deleteById(utente.getId());
        utenteRepo.save(encodedUtente);
        
        return getlistaUtentiAdmin();
}

    @GetMapping(value="/listaModificheAdmin")
    public ModelAndView getListaModifiche() {
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

        mv.addObject("lista_modifiche", lista_modifiche_dto);
        mv.setViewName("listaModificheAdmin");
        return mv;
    }
    
    @PostMapping(value="/accettaModifica/{id}")
    public ModelAndView accettaModifica(@PathVariable("id") Long id_modifica) {
        modifica modifica = modificaRepo.getReferenceById(id_modifica);

        spesa spesa_aggiornata = new spesa(
            modifica.getVecchiaSpesa().getId(), 
            modifica.getVecchiaSpesa().getUtente(), 
            modifica.getNuovoTotale(),
            modifica.getNuovaData(),
            modifica.getNuovaDescrizione());

        modificaRepo.delete(modifica);
        speseRepo.delete(modifica.getVecchiaSpesa());
        speseRepo.save(spesa_aggiornata);
        return getListaModifiche();
    }

    @PostMapping(value="rifiutaModifica/{id}")
    public ModelAndView rifiutaModifica(@PathVariable("id") Long id_modifica) {
        modificaRepo.delete(modificaRepo.getReferenceById(id_modifica));
        return getListaModifiche();
    }
    
    @PostMapping(value="cancellaSpesa/{id}")
    public ModelAndView rimuoviSpesa(@PathVariable("id") Long id_spesa) {
        if(modificaRepo.existsByVecchiaSpesa(speseRepo.getReferenceById(id_spesa))){
            return ComponiListaSpese(true);
        }
        speseRepo.delete(speseRepo.getReferenceById(id_spesa));
        return getListaSpeseAdmin();
    }
    

    private ModelAndView ComponiListaSpese(boolean errore){
        List<spesa> allspese = speseRepo.findAll();
        List<dtoSpesaConMatricola> allspesematricola = new ArrayList<>();
        Integer contatoreSpese = 0;
        Float Saldo = 0F;

        for (spesa spesa : allspese) {
            dtoSpesaConMatricola dto = new dtoSpesaConMatricola(spesa.getId(), spesa.getTotale(), spesa.getData(), spesa.getUtente().getMatricola(), Encode.forHtml(spesa.getDescription()));
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
}
