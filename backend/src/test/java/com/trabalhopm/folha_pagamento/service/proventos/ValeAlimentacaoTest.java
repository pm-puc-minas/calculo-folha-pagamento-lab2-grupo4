package com.trabalhopm.folha_pagamento.service.proventos;

import com.trabalhopm.folha_pagamento.domain.Funcionario;
import com.trabalhopm.folha_pagamento.service.provento.ValeAlimentacao;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.YearMonth;

import static org.junit.jupiter.api.Assertions.*;

public class ValeAlimentacaoTest {

    private final BigDecimal VALOR_DIARIO = new BigDecimal("32");
    ValeAlimentacao valeAlimentacao = new ValeAlimentacao();
    Funcionario funcionario = null;

    @Test
    @DisplayName("Mes com 23 dias uteis")
    public void calculoValeAlimentacaoTest() throws Exception {
        YearMonth yearMonth = YearMonth.of(2025,10);
        int diasUteis = 23;

        BigDecimal valorAlimentacao = VALOR_DIARIO.multiply(new BigDecimal(diasUteis));
        assertEquals(valorAlimentacao, valeAlimentacao.calcular(null, yearMonth));
    }
}
