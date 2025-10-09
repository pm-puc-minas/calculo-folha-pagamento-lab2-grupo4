package com.trabalhopm.folha_pagamento.service.provento;

import com.trabalhopm.folha_pagamento.domain.Funcionario;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.YearMonth;

@Component
public class Insalubridade implements IProvento {
    private final BigDecimal ADICIONAL_BAIXO = new BigDecimal("0.1");
    private final BigDecimal ADICIONAL_MEDIO = new BigDecimal("0.2");
    private final BigDecimal ADICIONAL_ALTO = new BigDecimal("0.4");
    private final BigDecimal SALARIO_MINIMO = new BigDecimal("1518");

    @Override
    public BigDecimal calcular(Funcionario funcionario, YearMonth mesReferencia) {
        return switch (funcionario.getNivelInsalubridade()) {
            case BAIXO -> SALARIO_MINIMO.multiply(ADICIONAL_BAIXO);
            case MEDIO -> SALARIO_MINIMO.multiply(ADICIONAL_MEDIO);
            case ALTO -> SALARIO_MINIMO.multiply(ADICIONAL_ALTO);
            default -> BigDecimal.ZERO;
        };
    }

    @Override
    public String getNome() {
        return this.getClass().getSimpleName();
    }
}