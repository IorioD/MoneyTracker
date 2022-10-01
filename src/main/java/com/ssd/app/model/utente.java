package com.ssd.app.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Component
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name="users")
public class utente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name="first_name",length = 20,nullable = false)
    private String nome;
    @Column(name="second_name",length = 20,nullable = false)
    private String cognome;
    @Column(name="matricola",length = 20,nullable = false,unique = true)
    private String matricola;
    @Column(name="pin",nullable = false)
    private String pin; 
    @Column(name="role",nullable = false)
    private String ruolo;

}
