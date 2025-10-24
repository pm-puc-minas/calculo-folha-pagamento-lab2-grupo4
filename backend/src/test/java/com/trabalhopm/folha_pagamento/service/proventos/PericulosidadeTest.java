package com.trabalhopm.folha_pagamento.service.proventos;

import com.trabalhopm.folha_pagamento.domain.Financeiro;
import com.trabalhopm.folha_pagamento.domain.Funcionario;
import com.trabalhopm.folha_pagamento.service.provento.Periculosidade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.YearMonth;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PericulosidadeTest {

    private Periculosidade periculosidade;
    private Funcionario funcionario;
    private final BigDecimal TAXA_ADICIONAL = new BigDecimal("0.3"); // 30%
    private final YearMonth mesReferencia = YearMonth.of(2025, 1);

    @BeforeEach
    void setUp() {
        periculosidade = new Periculosidade();
        funcionario = new Funcionario();
        // Configuração de um Financeiro básico para o funcionário
        funcionario.setFinanceiro(new Financeiro(null, new BigDecimal("5000.00"), 8.0, (byte) 5, BigDecimal.ZERO, funcionario));
    }

    @Test
    @DisplayName("Deve calcular o adicional de periculosidade quando o funcionário possuir a condição")
    void deveCalcularAdicional() {
        // Arrange
        BigDecimal salarioBruto = new BigDecimal("5000.00");
        funcionario.getFinanceiro().setSalarioBruto(salarioBruto);
        funcionario.setTemPericulosidade(true);

        // 5000.00 * 0.3 = 1500.00
        BigDecimal adicionalEsperado = salarioBruto.multiply(TAXA_ADICIONAL);

        // Act
        BigDecimal resultado = periculosidade.calcular(funcionario, mesReferencia);

        // Assert
        assertEquals(0, adicionalEsperado.compareTo(resultado),
                "O adicional de periculosidade está incorreto.");
    }

    @Test
    @DisplayName("Deve retornar ZERO quando o funcionário NÃO possuir a condição de periculosidade")
    void deveRetornarZeroQuandoNaoTemPericulosidade() {
        // Arrange
        funcionario.getFinanceiro().setSalarioBruto(new BigDecimal("5000.00"));
        funcionario.setTemPericulosidade(false);
        BigDecimal esperado = BigDecimal.ZERO;

        // Act
        BigDecimal resultado = periculosidade.calcular(funcionario, mesReferencia);

        // Assert
        assertEquals(0, esperado.compareTo(resultado),
                "O adicional deveria ser ZERO quando não há periculosidade.");
    }

    @Test
    @DisplayName("Deve calcular o adicional corretamente para outro valor de salário")
    void deveCalcularOutroSalario() {
        // Arrange
        BigDecimal salarioBruto = new BigDecimal("8250.75");
        funcionario.getFinanceiro().setSalarioBruto(salarioBruto);
        funcionario.setTemPericulosidade(true);
        
        // 8250.75 * 0.3 = 2475.225 (arredondando para 2475.23, se o BigDecimal usar a escala padrão, que neste caso não está definida, mas é melhor garantir com compareTo)
        BigDecimal adicionalEsperado = new BigDecimal("2475.225").setScale(2, BigDecimal.ROUND_HALF_UP);
        
        // O código de Periculosidade não usa setScale, então usamos o cálculo direto
        adicionalEsperado = salarioBruto.multiply(TAXA_ADICIONAL);
        
        // Act
        BigDecimal resultado = periculosidade.calcular(funcionario, mesReferencia);

        // Assert
        assertEquals(0, adicionalEsperado.compareTo(resultado),
                "O adicional para o salário de 8250.75 está incorreto.");
    }
    
    @Test
    @DisplayName("Deve retornar o nome da classe")
    void deveRetornarNomeCorreto() {
        assertEquals("Periculosidade", periculosidade.getNome());
    }
}
