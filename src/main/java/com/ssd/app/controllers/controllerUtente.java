package com.ssd.app.controllers;

import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.StringTokenizer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.ssd.app.model.dtoModifica;
import com.ssd.app.model.dtoNuovaSpesa;
import com.ssd.app.model.modifica;
import com.ssd.app.model.spesa;
import com.ssd.app.repository.modificaRepo;
import com.ssd.app.repository.speseRepo;
import com.ssd.app.repository.utenteRepo;

@Controller
public class controllerUtente {

    private ModelAndView mv;

    @Autowired
    private speseRepo speseRepo;

    @Autowired
    private utenteRepo utenteRepo;

    @Autowired
    private modificaRepo modificaRepo;

    public controllerUtente() {
        mv = new ModelAndView();
    }

    @GetMapping("/lista_spese")
    public ModelAndView getListaSpese( Principal principal) {
        if (principal != null) {
            mv.addObject("error_modifica_pending",false);
            mv.addObject("lista_spese", speseRepo.findAllByUtente(utenteRepo.findByMatricola(principal.getName())));
            mv.setViewName("listaSpese");
            return mv;
        }
        mv.setViewName("index");
        return mv;
    }

    @GetMapping("/formaddspesa")
    public ModelAndView getFormAddSpesa() {
        dtoNuovaSpesa spesa = new dtoNuovaSpesa();
        mv.addObject("spesa", spesa);
        mv.setViewName("addspesa");
        return mv;
    }

    @PostMapping("/saveSpesa")
    public ModelAndView saveSpesa(Principal principal, @ModelAttribute("spesa") dtoNuovaSpesa dtospesa) {
        if (principal != null) {
            StringTokenizer tokenizer = new StringTokenizer(dtospesa.getData(), "T");
            String sdata = tokenizer.nextToken();
            String sora = tokenizer.nextToken();

            String fulldate = sdata + " " + sora;

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm");
            LocalDateTime data = LocalDateTime.parse(fulldate, formatter);
            spesa spesa = new spesa(utenteRepo.findByMatricola(principal.getName()), dtospesa.getTotale(),
                    dtospesa.getDescription(), data);
            speseRepo.save(spesa);

            return getListaSpese(principal);
        }
        mv.setViewName("index");
        return mv;
    }

    @GetMapping("/formRichiestaModifica/{id}")
    public ModelAndView getFormRichiestaModifica(Principal principal,@PathVariable("id") Long id_spesa) {
        if (principal != null){
        spesa spesa = speseRepo.getReferenceById(id_spesa);

        if(!modificaRepo.existsByVecchiaSpesa(spesa)){
            dtoModifica modifica= new dtoModifica(id_spesa, spesa.getTotale(), spesa.getData().toString(), spesa.getDescription());
            mv.addObject("vecchia", spesa);
            mv.addObject("nuova", modifica);
            mv.setViewName("richiestaModifica");
            return mv;
        }
            mv.addObject("lista_spese", speseRepo.findAllByUtente(utenteRepo.findByMatricola(principal.getName())));
            mv.addObject("error_modifica_pending",true);
            mv.setViewName("listaSpese");
            return mv;
        }
        mv.setViewName("index");
        return mv;
    }

    @PostMapping("/saveRichiestaModifica")
    public ModelAndView saveRichiestaModifica(Principal principal, @ModelAttribute("nuova") dtoModifica dtomodifica) {
        if (principal != null) {
           
            StringTokenizer tokenizer = new StringTokenizer(dtomodifica.getNuovaData(), "T");
            String sdata = tokenizer.nextToken();
            String sora = tokenizer.nextToken();
            String fulldate = sdata + " " + sora;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm");
            LocalDateTime data = LocalDateTime.parse(fulldate, formatter);

            spesa vecchiaSpesa = speseRepo.getReferenceById(dtomodifica.getIdvecchiaSpesa());

            modifica modifica = new modifica(vecchiaSpesa,dtomodifica.getNuovoTotale(),data , dtomodifica.getNuovaDescrizione());

            modificaRepo.save(modifica);
            return getListaSpese(principal);

        }
        mv.setViewName("index");
        return mv;
    }

}
