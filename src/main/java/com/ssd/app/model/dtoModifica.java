package com.ssd.app.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class dtoModifica {
    private Long idvecchiaSpesa;
    private float nuovoTotale;
    private String nuovaData;
    private String nuovaDescrizione;
}