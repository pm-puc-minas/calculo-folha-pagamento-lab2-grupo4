package com.trabalhopm.folha_pagamento.service.desconto;

import com.trabalhopm.folha_pagamento.domain.Funcionario;
import com.trabalhopm.folha_pagamento.utils.Feriado.FeriadosNacionais;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

public class ValeTransporte implements IDesconto {

    private static final BigDecimal TAXA_SALARIAL = new BigDecimal("0.06");

    @Override
    public BigDecimal calcular(BigDecimal valor) {
        // Versão simples — ainda compatível com interface antiga
        return valor.multiply(TAXA_SALARIAL);
    }

    
    public BigDecimal calcular(Funcionario funcionario, YearMonth mesReferencia) throws Exception {
        int diasUteis = 0;
        int diasDoMes = mesReferencia.lengthOfMonth();

        
        List<LocalDate> feriadosDoMes = FeriadosNacionais.feriadosNoMes(mesReferencia.atDay(1));

        System.out.println("=== Cálculo Vale Transporte ===");
        for (int dia = 1; dia <= diasDoMes; dia++) {
            LocalDate dataAtual = mesReferencia.atDay(dia);
            DayOfWeek diaDaSemana = dataAtual.getDayOfWeek();

            boolean isFinalDeSemana = diaDaSemana == DayOfWeek.SATURDAY || diaDaSemana == DayOfWeek.SUNDAY;
            boolean isFeriado = feriadosDoMes.contains(dataAtual);

            if (!isFinalDeSemana && !isFeriado) {
                diasUteis++;
                System.out.println(dataAtual + " - Dia útil");
            } else {
                System.out.println(dataAtual + " - Não útil (" +
                        (isFinalDeSemana ? "Final de semana" : "Feriado") + ")");
            }
        }

        BigDecimal valorDiario = funcionario.getFinanceiro().getValorDiarioValeTransporte();
        BigDecimal custoMensal = valorDiario.multiply(BigDecimal.valueOf(diasUteis));

        System.out.println("Dias úteis: " + diasUteis);
        System.out.println("Valor diário VT: R$ " + valorDiario);
        System.out.println("Custo total VT: R$ " + custoMensal);

        
        BigDecimal descontoMaximo = funcionario.getFinanceiro().getSalarioBruto().multiply(TAXA_SALARIAL);

        
        return custoMensal.min(descontoMaximo);
    }

    @Override
    public String getNome() {
        return this.getClass().getSimpleName();
    }
}
