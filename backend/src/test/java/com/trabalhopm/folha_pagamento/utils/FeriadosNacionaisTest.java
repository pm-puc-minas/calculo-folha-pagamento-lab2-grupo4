package com.trabalhopm.folha_pagamento.utils;

import com.trabalhopm.folha_pagamento.utils.Feriado.FeriadosNacionais;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

public class FeriadosNacionaisTest {

    @Test
    @DisplayName("1 feriado em dezembro de 2025")
    public void umFeriado() throws Exception {
        LocalDate dataInserida = LocalDate.of(2025, 12, 1);
        LocalDate dataFeriado = LocalDate.of(2025, 12, 25);

        assertEquals(dataFeriado, FeriadosNacionais.feriadosNoMes(dataInserida).getFirst());
    }
}