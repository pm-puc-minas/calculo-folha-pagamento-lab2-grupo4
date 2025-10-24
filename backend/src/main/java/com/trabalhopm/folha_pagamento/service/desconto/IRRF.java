package com.trabalhopm.folha_pagamento.service.desconto;


import com.trabalhopm.folha_pagamento.domain.Funcionario;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.YearMonth;

@Component
public class IRRF implements IDesconto {
    private static final BigDecimal FAIXA1 = new BigDecimal("2259.20");
    private static final BigDecimal FAIXA2 = new BigDecimal("3451.05");

    private static final BigDecimal TAXA1 = BigDecimal.ZERO;
    private static final BigDecimal TAXA2 = new BigDecimal("0.15");
    private static final BigDecimal TAXA3 = new BigDecimal("0.225");

    @Override
    public BigDecimal calcular(Funcionario funcionario, YearMonth mesReferencia) {
        BigDecimal valor = funcionario.getFinanceiro().getSalarioBruto();

        if (valor.compareTo(FAIXA1) <= 0) return valor.multiply(TAXA1);
        else if (valor.compareTo(FAIXA2) <= 0) return valor.multiply(TAXA2);
        else return valor.multiply(TAXA3);
    }

    @Override
    public String getNome() {
        return this.getClass().getSimpleName();
    }
}

