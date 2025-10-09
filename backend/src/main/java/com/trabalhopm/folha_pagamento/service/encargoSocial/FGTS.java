package com.trabalhopm.folha_pagamento.service.encargoSocial;

import com.trabalhopm.folha_pagamento.domain.Funcionario;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class FGTS implements IEncargoSocial {

    private final BigDecimal taxaRecolhimento = new BigDecimal("0.08");

    @Override
    public BigDecimal calcular(Funcionario funcionario) {
        return funcionario.getFinanceiro().getSalarioBruto().multiply(taxaRecolhimento);
    }

    @Override
    public String getNome() {
        return this.getClass().getSimpleName();
    }
}