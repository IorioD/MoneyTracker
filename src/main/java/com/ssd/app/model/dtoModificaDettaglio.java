package com.ssd.app.model;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class dtoModificaDettaglio {
    private Long id_modifica;
    private float totale_modifica;
    private LocalDateTime data_modifica;
    private String descrizione_modifica;

    private Long id_spesa;
    private float totale_spesa;
    private LocalDateTime data_spesa;
    private String descrizione_spesa;
}
