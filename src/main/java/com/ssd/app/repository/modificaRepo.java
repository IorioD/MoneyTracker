package com.ssd.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ssd.app.model.modifica;
import com.ssd.app.model.spesa;

@Repository
public interface modificaRepo extends JpaRepository<modifica,Long>{
    boolean existsByVecchiaSpesa(spesa spesa);
}
