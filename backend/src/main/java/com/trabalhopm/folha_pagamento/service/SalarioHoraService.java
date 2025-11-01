package com.trabalhopm.folha_pagamento.service;

import com.trabalhopm.folha_pagamento.domain.Funcionario;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class SalarioHoraService {

    // Constante para horas mensais padrão (220 horas para 44h/semana)
    private static final BigDecimal HORAS_MES_PADRAO = new BigDecimal("220");
    // Precisão de 4 casas decimais para o valor da hora, conforme boas práticas de cálculo.
    private static final int SCALE = 4;

    /**
     * Calcula o valor do salário por hora (Salário/Hora) de um funcionário.
     * O cálculo é feito dividindo o salário bruto mensal pela base de horas mensais.
     * * @param funcionario O objeto Funcionario contendo o Salário Bruto.
     * @return O valor do salário por hora com precisão de 4 casas decimais.
     */
    public BigDecimal calcularSalarioHora(Funcionario funcionario) {
        // Verifica se os dados necessários estão disponíveis
        if (funcionario == null || 
            funcionario.getFinanceiro() == null || 
            funcionario.getFinanceiro().getSalarioBruto() == null ||
            funcionario.getFinanceiro().getSalarioBruto().compareTo(BigDecimal.ZERO) <= 0) {
            
            return BigDecimal.ZERO;
        }

        BigDecimal salarioBruto = funcionario.getFinanceiro().getSalarioBruto();

        // Realiza a divisão com arredondamento
        return salarioBruto.divide(
            HORAS_MES_PADRAO, 
            SCALE, 
            RoundingMode.HALF_UP
        );
    }
}
