package com.ssd.app.model;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class modifica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="spesa_id",referencedColumnName = "id")
    private spesa vecchiaSpesa;

    private float nuovoTotale;
    private LocalDateTime nuovaData;
    private String nuovaDescrizione;

    public modifica(spesa vecchiaspesa,float nuovoTotale, LocalDateTime nuovaData, String nuovaDescrizione){
        this.vecchiaSpesa = vecchiaspesa;
        this.nuovoTotale =nuovoTotale;
        this.nuovaData = nuovaData;
        this.nuovaDescrizione=nuovaDescrizione;
    }
}
