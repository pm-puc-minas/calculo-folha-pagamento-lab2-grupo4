package com.trabalhopm.folha_pagamento.service.provento;


import com.trabalhopm.folha_pagamento.domain.Funcionario;
import com.trabalhopm.folha_pagamento.utils.Feriado.FeriadosNacionais;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

public class ValeAlimentacao implements IProvento {
    private final BigDecimal VALOR_DIARIO = new BigDecimal("32");

    @Override
    public BigDecimal calcular(Funcionario funcionario, YearMonth mesReferencia) throws Exception {
        int diasUteis = 0;

        int diasDoMes = mesReferencia.lengthOfMonth();

        List<LocalDate> feriadosDoMes = FeriadosNacionais.feriadosNoMes(mesReferencia.atDay(1));

        for(int dia = 1; dia < diasDoMes; dia++){
            LocalDate dataAtual = mesReferencia.atDay(dia);
            DayOfWeek diaDaSemana = dataAtual.getDayOfWeek();

            boolean isFinalDeSemana = diaDaSemana == DayOfWeek.SATURDAY || diaDaSemana == DayOfWeek.SUNDAY;

            boolean isFeriado = feriadosDoMes.contains(dataAtual);

            if (!isFinalDeSemana && !isFeriado) {
                diasUteis++;
            }
        }

        return VALOR_DIARIO.multiply(BigDecimal.valueOf(diasUteis));
    }

    @Override
    public String getNome() {
        return this.getClass().getSimpleName();
    }
}
