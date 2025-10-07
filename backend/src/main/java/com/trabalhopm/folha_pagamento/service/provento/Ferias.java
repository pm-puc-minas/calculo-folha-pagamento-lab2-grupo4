package com.trabalhopm.folha_pagamento.service.provento;

import com.trabalhopm.folha_pagamento.domain.Funcionario;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class Ferias implements Provento {
    private final BigDecimal UM_TERCO = new BigDecimal("1").divide(new BigDecimal("3"), 10, RoundingMode.HALF_UP);

    @Override
    public BigDecimal calcular(Funcionario funcionario) {
        BigDecimal tercoConstitucional = funcionario.getFinanceiro().getSalarioBruto().multiply(UM_TERCO);
        return funcionario.getFinanceiro().getSalarioBruto().add(tercoConstitucional).setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public String getNome(){
        return "Ferias";
    }
}
