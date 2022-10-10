package com.ssd.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ssd.app.model.spesa;


@Repository
public interface speseRepo extends JpaRepository<spesa,Long> {
    List<spesa> findAllByMatricola(String matricola);
}
