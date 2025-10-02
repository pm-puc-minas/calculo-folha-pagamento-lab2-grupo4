package com.trabalhopm.folha_pagamento.service.desconto;


import java.math.BigDecimal;

public class INSS implements Desconto {
    private static final BigDecimal FAIXA1 = new BigDecimal("1518.00");
    private static final BigDecimal FAIXA2 = new BigDecimal("2793.88");

    private static final BigDecimal TAXA1 = new BigDecimal("0.075");
    private static final BigDecimal TAXA2 = new BigDecimal("0.09");
    private static final BigDecimal TAXA3 = new BigDecimal("0.14");

    @Override
    public BigDecimal calcular(BigDecimal valor) {
        if (valor.compareTo(FAIXA1) <= 0) return valor.multiply(TAXA1);
        else if (valor.compareTo(FAIXA2) <= 0) return valor.multiply(TAXA2);
        else return valor.multiply(TAXA3);
    }

    @Override
    public String getNome() {
        return "INSS";
    }
}
