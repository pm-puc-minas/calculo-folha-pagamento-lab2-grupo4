package com.trabalhopm.folha_pagamento.service.provento;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

import com.trabalhopm.folha_pagamento.domain.Funcionario;
import com.trabalhopm.folha_pagamento.service.desconto.DescontoValeTransporte;
import com.trabalhopm.folha_pagamento.utils.Feriado.FeriadosNacionais;
import org.springframework.stereotype.Component;

@Component
public class ProventoValeTransporte implements IProvento {

    @Override
    public BigDecimal calcular(Funcionario funcionario, YearMonth mesReferencia) throws Exception {

        int diasUteis = 0;
        int diasDoMes = mesReferencia.lengthOfMonth();

        List<LocalDate> feriadosDoMes = FeriadosNacionais.feriadosNoMes(mesReferencia.atDay(1));

        for (int dia = 1; dia <= diasDoMes; dia++) {
            LocalDate dataAtual = mesReferencia.atDay(dia);
            DayOfWeek diaDaSemana = dataAtual.getDayOfWeek();

            boolean isFinalDeSemana = diaDaSemana == DayOfWeek.SATURDAY || diaDaSemana == DayOfWeek.SUNDAY;
            boolean isFeriado = feriadosDoMes.contains(dataAtual);

            if (!isFinalDeSemana && !isFeriado) {
                diasUteis++;
            }
        }

        BigDecimal valorDiario = funcionario.getFinanceiro().getValorDiarioValeTransporte();
        BigDecimal custoMensal = valorDiario.multiply(BigDecimal.valueOf(diasUteis));

        return custoMensal;
    }

    @Override
    public String getNome() {
        return this.getClass().getSimpleName();
    }
}