package com.trabalhopm.folha_pagamento.service.desconto;


import java.math.BigDecimal;

public class ValeTransporte implements IDesconto {
    private static final BigDecimal TAXA = new BigDecimal("0.06");

    @Override
    public BigDecimal calcular(BigDecimal valor) {
        return valor.multiply(TAXA);
    }

    @Override
    public String getNome() {
        return this.getClass().getSimpleName();
    }
}
