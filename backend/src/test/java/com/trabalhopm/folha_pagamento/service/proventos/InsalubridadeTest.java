package com.trabalhopm.folha_pagamento.service.proventos;

import com.trabalhopm.folha_pagamento.domain.Financeiro;
import com.trabalhopm.folha_pagamento.domain.Funcionario;
import com.trabalhopm.folha_pagamento.domain.enums.NivelInsalubridade;
import com.trabalhopm.folha_pagamento.service.provento.Insalubridade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.YearMonth;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class InsalubridadeTest {

    private Insalubridade insalubridade;
    private Funcionario funcionario;
    private final YearMonth mesReferencia = YearMonth.of(2025, 1);
    private final BigDecimal SALARIO_MINIMO = new BigDecimal("1518.00");

    @BeforeEach
    void setUp() {
        insalubridade = new Insalubridade();
        funcionario = new Funcionario();
        // Um objeto Financeiro é necessário, mesmo que o salário bruto não seja usado diretamente
        // no cálculo de Insalubridade, mas é uma dependência da classe Funcionario.
        funcionario.setFinanceiro(new Financeiro(null, new BigDecimal("2000.00"), 8.0, (byte) 5, BigDecimal.ZERO, funcionario));
    }

    @Test
    @DisplayName("Deve calcular o adicional para o Nível BAIXO (10% do salário mínimo)")
    void deveCalcularAdicionalBaixo() {
        // Arrange
        funcionario.setNivelInsalubridade(NivelInsalubridade.BAIXO);
        BigDecimal adicionalEsperado = SALARIO_MINIMO.multiply(new BigDecimal("0.1"));
        
        // Act
        BigDecimal resultado = insalubridade.calcular(funcionario, mesReferencia);

        // Assert
        // O cálculo é 1518 * 0.1 = 151.80
        assertEquals(0, adicionalEsperado.setScale(2, RoundingMode.HALF_UP).compareTo(resultado),
                "O adicional de insalubridade (BAIXO) está incorreto.");
    }

    @Test
    @DisplayName("Deve calcular o adicional para o Nível MEDIO (20% do salário mínimo)")
    void deveCalcularAdicionalMedio() {
        // Arrange
        funcionario.setNivelInsalubridade(NivelInsalubridade.MEDIO);
        BigDecimal adicionalEsperado = SALARIO_MINIMO.multiply(new BigDecimal("0.2"));

        // Act
        BigDecimal resultado = insalubridade.calcular(funcionario, mesReferencia);

        // Assert
        // O cálculo é 1518 * 0.2 = 303.60
        assertEquals(0, adicionalEsperado.setScale(2, RoundingMode.HALF_UP).compareTo(resultado),
                "O adicional de insalubridade (MEDIO) está incorreto.");
    }

    @Test
    @DisplayName("Deve calcular o adicional para o Nível ALTO (40% do salário mínimo)")
    void deveCalcularAdicionalAlto() {
        // Arrange
        funcionario.setNivelInsalubridade(NivelInsalubridade.ALTO);
        BigDecimal adicionalEsperado = SALARIO_MINIMO.multiply(new BigDecimal("0.4"));

        // Act
        BigDecimal resultado = insalubridade.calcular(funcionario, mesReferencia);

        // Assert
        // O cálculo é 1518 * 0.4 = 607.20
        assertEquals(0, adicionalEsperado.setScale(2, RoundingMode.HALF_UP).compareTo(resultado),
                "O adicional de insalubridade (ALTO) está incorreto.");
    }
    
    @Test
    @DisplayName("Deve retornar ZERO quando o nível de insalubridade for NENHUM")
    void deveRetornarZeroQuandoNenhum() {
        // Arrange
        funcionario.setNivelInsalubridade(NivelInsalubridade.NENHUM);
        BigDecimal esperado = BigDecimal.ZERO;

        // Act
        BigDecimal resultado = insalubridade.calcular(funcionario, mesReferencia);

        // Assert
        assertEquals(0, esperado.compareTo(resultado),
                "O adicional deveria ser ZERO para NENHUM nível de insalubridade.");
    }

    @Test
    @DisplayName("Deve retornar o nome da classe")
    void deveRetornarNomeCorreto() {
        assertEquals("Insalubridade", insalubridade.getNome());
    }
}
