package com.ssd.app.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

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
@Table(name = "spese")
public class spesa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name="user_id",referencedColumnName = "id")
    private utente utente;
    @Column(name="totale",length = 20,nullable = false)
    private float totale;
    @Column(name="data",length = 20,nullable = false)
    private LocalDateTime data;
    @Column(name="description",length = 20,nullable = false)
    private String description;

    public spesa(utente utente, float totale, String description,int giorno, int mese, int anno, int ora, int minuti){
        LocalDate date = LocalDate.of(anno, mese, giorno);
        LocalTime time = LocalTime.of(ora, minuti);
        this.data = LocalDateTime.of(date, time);
        this.utente=utente;
        this.totale=totale;
        this.description=description;
    }

    public spesa(utente utente, float totale, String description, LocalDateTime data){
        this.data=data;
        this.utente=utente;
        this.totale=totale;
        this.description=description;
    }
}
