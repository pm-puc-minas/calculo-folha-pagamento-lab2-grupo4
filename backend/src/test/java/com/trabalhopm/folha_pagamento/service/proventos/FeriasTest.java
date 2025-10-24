package com.trabalhopm.folha_pagamento.service.proventos;

import com.trabalhopm.folha_pagamento.domain.Financeiro;
import com.trabalhopm.folha_pagamento.domain.Funcionario;
import com.trabalhopm.folha_pagamento.service.provento.Ferias;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.YearMonth;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FeriasTest {

    private Ferias ferias;
    private Funcionario funcionario;
    private final YearMonth mesReferencia = YearMonth.of(2025, 1);
    private final BigDecimal UM_TERCO = new BigDecimal("1").divide(new BigDecimal("3"), 10, RoundingMode.HALF_UP);


    @BeforeEach
    void setUp() {
        ferias = new Ferias();
        funcionario = new Funcionario();
        // Configuração de um Financeiro base (o salário bruto será alterado em cada teste)
        funcionario.setFinanceiro(new Financeiro(null, BigDecimal.ZERO, 8.0, (byte) 5, BigDecimal.ZERO, funcionario));
    }

    @Test
    @DisplayName("Deve calcular 1/3 do salário bruto exatamente (sem arredondamento)")
    void deveCalcularFeriasExatamente() {
        // Arrange
        BigDecimal salarioBruto = new BigDecimal("3000.00");
        funcionario.getFinanceiro().setSalarioBruto(salarioBruto);
        
        // 3000.00 / 3 = 1000.00
        BigDecimal esperado = new BigDecimal("1000.00");

        // Act
        BigDecimal resultado = ferias.calcular(funcionario, mesReferencia);

        // Assert
        assertEquals(0, esperado.compareTo(resultado),
                "O terço constitucional (cálculo exato) está incorreto.");
    }

    @Test
    @DisplayName("Deve calcular 1/3 do salário bruto e arredondar para cima (5000 / 3 = 1666.67)")
    void deveCalcularFeriasComArredondamentoParaCima() {
        // Arrange
        BigDecimal salarioBruto = new BigDecimal("5000.00");
        funcionario.getFinanceiro().setSalarioBruto(salarioBruto);
        
        // 5000.00 * (1/3) = 1666.6666... -> 1666.67 (HALF_UP)
        BigDecimal esperado = new BigDecimal("1666.67");

        // Act
        BigDecimal resultado = ferias.calcular(funcionario, mesReferencia);

        // Assert
        assertEquals(0, esperado.compareTo(resultado),
                "O terço constitucional (arredondamento para cima) está incorreto.");
    }
    
    @Test
    @DisplayName("Deve calcular 1/3 do salário bruto e arredondar para baixo (7000 / 3 = 2333.33)")
    void deveCalcularFeriasComArredondamentoParaBaixo() {
        // Arrange
        BigDecimal salarioBruto = new BigDecimal("7000.00");
        funcionario.getFinanceiro().setSalarioBruto(salarioBruto);

        // 7000.00 * (1/3) = 2333.3333... -> 2333.33 (HALF_UP)
        BigDecimal esperado = new BigDecimal("2333.33");

        // Act
        BigDecimal resultado = ferias.calcular(funcionario, mesReferencia);

        // Assert
        assertEquals(0, esperado.compareTo(resultado),
                "O terço constitucional (arredondamento para baixo) está incorreto.");
    }

    @Test
    @DisplayName("Deve retornar o nome da classe")
    void deveRetornarNomeCorreto() {
        assertEquals("Ferias", ferias.getNome());
    }
}
