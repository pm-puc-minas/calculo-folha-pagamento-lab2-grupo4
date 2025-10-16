package com.trabalhopm.folha_pagamento.service.provento;

import com.trabalhopm.folha_pagamento.domain.Funcionario;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.YearMonth;

@Component
public class Ferias implements IProvento {
    private final BigDecimal UM_TERCO = new BigDecimal("1").divide(new BigDecimal("3"), 10, RoundingMode.HALF_UP);

    @Override
    public BigDecimal calcular(Funcionario funcionario, YearMonth mesReferencia) {
        if(!funcionario.isDeFerias()){
            return BigDecimal.ZERO;
        }

        BigDecimal tercoConstitucional = funcionario.getFinanceiro().getSalarioBruto().multiply(UM_TERCO);
        return tercoConstitucional.setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public String getNome(){
        return this.getClass().getSimpleName();
    }
}
