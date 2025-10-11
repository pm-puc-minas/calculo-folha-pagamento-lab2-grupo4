package com.trabalhopm.folha_pagamento.service.desconto;

import com.trabalhopm.folha_pagamento.service.CalculadoraSalario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

class CalculadoraSalarioTest {

    private CalculadoraSalario calculadora;
    private BigDecimal salarioBruto;

    @BeforeEach
    void setup() {
        calculadora = new CalculadoraSalario();
        salarioBruto = new BigDecimal("3000.00");
    }

    @Test
    void deveCalcularSalarioLiquidoComINSS_IRRF_VT() throws Exception {
        // Arrange
        calculadora.adicionarDesconto(new INSS());
        calculadora.adicionarDesconto(new IRRF());
        calculadora.adicionarDesconto(new ValeTransporte());

        // Act
        BigDecimal salarioLiquido = calculadora.calcularSalarioLiquido(salarioBruto);

        // Assert
        // INSS: faixa3 (14%)
        BigDecimal inss = salarioBruto.multiply(new BigDecimal("0.14"));
        // IRRF: faixa3 (22.5%)
        BigDecimal irrf = salarioBruto.multiply(new BigDecimal("0.225"));
        // Vale transporte: 6%
        BigDecimal vt = salarioBruto.multiply(new BigDecimal("0.06"));

        BigDecimal totalDescontos = inss.add(irrf).add(vt);
        BigDecimal esperado = salarioBruto.subtract(totalDescontos);

        assertEquals(0, esperado.compareTo(salarioLiquido),
                "O cálculo do salário líquido está incorreto");
    }

    @Test
    void deveCalcularSalarioLiquidoSemDescontos() throws Exception {
        // Nenhum desconto adicionado
        BigDecimal resultado = calculadora.calcularSalarioLiquido(salarioBruto);

        assertEquals(0, salarioBruto.compareTo(resultado),
                "Salário líquido deve ser igual ao bruto quando não há descontos");
    }

    @Test
    void deveSomarCorretamenteDiversosDescontos() throws Exception {
        calculadora.adicionarDesconto(new ValeTransporte());
        calculadora.adicionarDesconto(new ValeTransporte()); // adicionado duas vezes só pra testar soma

        BigDecimal resultado = calculadora.calcularSalarioLiquido(salarioBruto);

        // Cada VT é 6% → 12% total
        BigDecimal totalDescontos = salarioBruto.multiply(new BigDecimal("0.12"));
        BigDecimal esperado = salarioBruto.subtract(totalDescontos);

        assertEquals(0, esperado.compareTo(resultado),
                "O total de descontos acumulados está incorreto");
    }
}
