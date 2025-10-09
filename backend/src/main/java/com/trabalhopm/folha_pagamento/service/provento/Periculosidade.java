package com.trabalhopm.folha_pagamento.service.provento;

import com.trabalhopm.folha_pagamento.domain.Funcionario;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.YearMonth;

@Component
public class Periculosidade implements IProvento {
    private final BigDecimal ADICIONAL = new BigDecimal("0.3");

    @Override
    public BigDecimal calcular(Funcionario funcionario, YearMonth mesReferencia) {
        if (!funcionario.isTemPericulosidade()) {
            return BigDecimal.ZERO;
        }
        return funcionario.getFinanceiro().getSalarioBruto().multiply(ADICIONAL);
    }

    @Override
    public String getNome() {
        return this.getClass().getSimpleName();
    }
}