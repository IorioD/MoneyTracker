package com.ssd.app.model;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class dtoSpesaConMatricola {
    private Long id;
    private float totale;
    private LocalDateTime data;
    private String matricola;
    private String descrizione;
}
