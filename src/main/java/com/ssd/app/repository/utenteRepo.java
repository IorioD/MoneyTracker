package com.ssd.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ssd.app.model.utente;

@Repository
public interface utenteRepo  extends JpaRepository<utente,Long>{
    utente findByMatricola(String Matricola);
    boolean existsByMatricola(String Matricola);
}
