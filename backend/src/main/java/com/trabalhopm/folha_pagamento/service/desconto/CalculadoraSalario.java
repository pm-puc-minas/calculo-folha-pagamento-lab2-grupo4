package com.trabalhopm.folha_pagamento.service.desconto;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class CalculadoraSalario {
    private final List<IDesconto> descontos = new ArrayList<>();

    public void adicionarDesconto(IDesconto desconto) {
        this.descontos.add(desconto);
    }

    public BigDecimal calcularSalarioLiquido(BigDecimal salarioBruto) throws Exception {
        BigDecimal totalDescontos = BigDecimal.ZERO;

        System.out.println("=== Relatório de Descontos ===");
        for (IDesconto d : descontos) {
            BigDecimal valorDesconto = d.calcular(salarioBruto);
            System.out.println(d.getNome() + ": R$ " + valorDesconto);
            totalDescontos = totalDescontos.add(valorDesconto);
        }
        System.out.println("Total de Descontos: R$ " + totalDescontos);

        return salarioBruto.subtract(totalDescontos);
    }
}
