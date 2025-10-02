package com.trabalhopm.folha_pagamento.service.desconto;


import java.math.BigDecimal;

public class ValeAlimentacao implements Desconto {
    private BigDecimal valorMensal;

    public ValeAlimentacao(BigDecimal valorMensal) {
        this.valorMensal = valorMensal;
    }

    @Override
    public BigDecimal calcular(BigDecimal valor) {
        return valorMensal;
    }

    @Override
    public String getNome() {
        return "Vale Alimentação";
    }
}
