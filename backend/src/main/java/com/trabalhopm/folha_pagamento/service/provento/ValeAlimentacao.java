package com.trabalhopm.folha_pagamento.service.provento;


import com.trabalhopm.folha_pagamento.domain.Funcionario;
import com.trabalhopm.folha_pagamento.utils.Feriado.FeriadosNacionais;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@Component
public class ValeAlimentacao implements IProvento {

    @Override
    public BigDecimal calcular(Funcionario funcionario, YearMonth mesReferencia) throws Exception {
        int diasUteis = 0;

        int diasDoMes = mesReferencia.lengthOfMonth();

        List<LocalDate> feriadosDoMes = FeriadosNacionais.feriadosNoMes(mesReferencia.atDay(1));

        for(int dia = 1; dia <= diasDoMes; dia++){
            LocalDate dataAtual = mesReferencia.atDay(dia);
            DayOfWeek diaDaSemana = dataAtual.getDayOfWeek();

            boolean isFinalDeSemana = diaDaSemana == DayOfWeek.SATURDAY || diaDaSemana == DayOfWeek.SUNDAY;

            boolean isFeriado = feriadosDoMes.contains(dataAtual);

            if (!isFinalDeSemana && !isFeriado) {
                diasUteis++;
            }
        }

        BigDecimal valorDiario = funcionario.getFinanceiro().getValorDiarioValeAlimentacao();

        return valorDiario.multiply(BigDecimal.valueOf(diasUteis));
    }

    @Override
    public String getNome() {
        return this.getClass().getSimpleName();
    }
}
