package com.trabalhopm.folha_pagamento.service.proventos;

import com.trabalhopm.folha_pagamento.domain.Financeiro;
import com.trabalhopm.folha_pagamento.domain.Funcionario;
import com.trabalhopm.folha_pagamento.service.provento.SalarioFamilia;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.YearMonth;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SalarioFamiliaTest {

    private SalarioFamilia salarioFamilia;
    private Funcionario funcionario;
    private final YearMonth mesReferencia = YearMonth.of(2025, 1);
    private final BigDecimal VALOR_COTA = new BigDecimal("65.00");
    private final BigDecimal TETO_SALARIAL = new BigDecimal("1906.04");

    @BeforeEach
    void setUp() {
        salarioFamilia = new SalarioFamilia();
        funcionario = new Funcionario();
        // Criação de um objeto Financeiro base
        funcionario.setFinanceiro(new Financeiro(null, BigDecimal.ZERO, 8.0, (byte) 5, BigDecimal.ZERO, funcionario));
    }

    @Test
    @DisplayName("Deve calcular o valor de Salário Família para um dependente com salário abaixo do teto")
    void deveCalcularComSalarioAbaixoDoTetoEUmDependente() throws Exception {
        // Arrange
        funcionario.getFinanceiro().setSalarioBruto(new BigDecimal("1500.00"));
        funcionario.setNumeroDeDependentes((byte) 1);
        BigDecimal esperado = VALOR_COTA;

        // Act
        BigDecimal resultado = salarioFamilia.calcular(funcionario, mesReferencia);

        // Assert
        assertEquals(0, esperado.compareTo(resultado),
                "O Salário Família para 1 dependente está incorreto.");
    }

    @Test
    @DisplayName("Deve calcular o valor de Salário Família para múltiplos dependentes com salário no limite do teto")
    void deveCalcularComSalarioNoLimiteDoTetoEMultiplosDependentes() throws Exception {
        // Arrange
        funcionario.getFinanceiro().setSalarioBruto(TETO_SALARIAL); // Salário no limite
        funcionario.setNumeroDeDependentes((byte) 3);
        // 3 dependentes * R$ 65,00 = R$ 195,00
        BigDecimal esperado = VALOR_COTA.multiply(new BigDecimal(3));

        // Act
        BigDecimal resultado = salarioFamilia.calcular(funcionario, mesReferencia);

        // Assert
        assertEquals(0, esperado.compareTo(resultado),
                "O Salário Família para 3 dependentes e salário no limite está incorreto.");
    }

    @Test
    @DisplayName("Deve retornar ZERO quando o salário bruto estiver ACIMA do teto salarial")
    void deveRetornarZeroQuandoSalarioAcimaDoTeto() throws Exception {
        // Arrange
        funcionario.getFinanceiro().setSalarioBruto(new BigDecimal("1906.05"));
        funcionario.setNumeroDeDependentes((byte) 2);
        BigDecimal esperado = BigDecimal.ZERO;

        // Act
        BigDecimal resultado = salarioFamilia.calcular(funcionario, mesReferencia);

        // Assert
        assertEquals(0, esperado.compareTo(resultado),
                "O Salário Família deveria ser ZERO para salário acima do teto.");
    }

    @Test
    @DisplayName("Deve retornar ZERO quando o funcionário não tiver dependentes")
    void deveRetornarZeroSemDependentes() throws Exception {
        // Arrange
        funcionario.getFinanceiro().setSalarioBruto(new BigDecimal("1500.00"));
        funcionario.setNumeroDeDependentes((byte) 0);
        BigDecimal esperado = BigDecimal.ZERO;

        // Act
        BigDecimal resultado = salarioFamilia.calcular(funcionario, mesReferencia);

        // Assert
        assertEquals(0, esperado.compareTo(resultado),
                "O Salário Família deveria ser ZERO sem dependentes.");
    }

    @Test
    @DisplayName("Deve retornar o nome da classe")
    void deveRetornarNomeCorreto() {
        assertEquals("SalarioFamilia", salarioFamilia.getNome());
    }
}
