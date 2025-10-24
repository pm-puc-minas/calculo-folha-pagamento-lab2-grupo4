package com.trabalhopm.folha_pagamento.service.provento;

import com.trabalhopm.folha_pagamento.domain.Funcionario;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.YearMonth;

@Component
public class SalarioFamilia implements IProvento {
    private final BigDecimal TETO_SALARIAL = new BigDecimal("1906.04");
    private final BigDecimal VALOR_COTA_POR_DEPENDENTE = new BigDecimal("65");

    @Override
    public BigDecimal calcular(Funcionario funcionario, YearMonth mesReferencia) {
        if(funcionario.getFinanceiro().getSalarioBruto().compareTo(TETO_SALARIAL) > 0){
            return BigDecimal.ZERO;
        }

        if(funcionario.getNumeroDeDependentes() == 0){
            return BigDecimal.ZERO;
        }

        BigDecimal numeroDeDependentes = new BigDecimal(funcionario.getNumeroDeDependentes());
        BigDecimal valorTotal = VALOR_COTA_POR_DEPENDENTE.multiply(numeroDeDependentes);

        return valorTotal.setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public String getNome(){
        return this.getClass().getSimpleName();
    }
}
