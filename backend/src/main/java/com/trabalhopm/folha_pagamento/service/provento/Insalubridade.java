package com.trabalhopm.folha_pagamento.service.provento;

import com.trabalhopm.folha_pagamento.domain.Funcionario;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class Insalubridade implements Provento {
    private final BigDecimal adicionalBaixo = new BigDecimal("0.1");
    private final BigDecimal adicionalMedio = new BigDecimal("0.2");
    private final BigDecimal adicionalAlto = new BigDecimal("0.4");
    private final BigDecimal salarioMinimo = new BigDecimal("1518");

    @Override
    public BigDecimal calcular(Funcionario funcionario) {
        return switch (funcionario.getNivelInsalubridade()) {
            case BAIXO -> salarioMinimo.multiply(adicionalBaixo);
            case MEDIO -> salarioMinimo.multiply(adicionalMedio);
            case ALTO -> salarioMinimo.multiply(adicionalAlto);
            default -> BigDecimal.ZERO;
        };
    }

    @Override
    public String getNome() {
        return "Insalubridade";
    }
}