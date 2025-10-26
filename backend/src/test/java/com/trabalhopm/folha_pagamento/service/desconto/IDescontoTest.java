package com.trabalhopm.folha_pagamento.service.desconto;

import com.trabalhopm.folha_pagamento.domain.Financeiro;
import com.trabalhopm.folha_pagamento.domain.Funcionario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.YearMonth;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class IDescontoTest {
    Funcionario funcionario;

    @BeforeEach
    void setUp() {
        Financeiro financeiro = new Financeiro();
        financeiro.setHorasTrabalhadasDia(8);
        financeiro.setValorDiarioValeTransporte(BigDecimal.valueOf(11));
        financeiro.setSalarioBruto(BigDecimal.ZERO);
        funcionario = new Funcionario();

        funcionario.setFinanceiro(financeiro);
    }


    @Test
    void deveCalcularINSSFaixa1() throws Exception {
        IDesconto inss = new INSS();
        funcionario.getFinanceiro().setSalarioBruto(BigDecimal.valueOf(1200));
        BigDecimal esperado = funcionario.getFinanceiro().getSalarioBruto().multiply(new BigDecimal("0.075"));
        BigDecimal resultado = inss.calcular(funcionario, null);
        assertEquals(0, esperado.compareTo(resultado),
                "INSS faixa 1 incorreto");
    }

    @Test
    void deveCalcularINSSFaixa2() throws Exception {
        IDesconto inss = new INSS();
        funcionario.getFinanceiro().setSalarioBruto(BigDecimal.valueOf(2000));
        BigDecimal esperado = funcionario.getFinanceiro().getSalarioBruto().multiply(new BigDecimal("0.09"));
        BigDecimal resultado = inss.calcular(funcionario, null);
        assertEquals(0, esperado.compareTo(resultado),
                "INSS faixa 2 incorreto");
    }

    @Test
    void deveCalcularINSSFaixa3() throws Exception {
        IDesconto inss = new INSS();
        funcionario.getFinanceiro().setSalarioBruto(BigDecimal.valueOf(4000));
        BigDecimal esperado = funcionario.getFinanceiro().getSalarioBruto().multiply(new BigDecimal("0.14"));
        BigDecimal resultado = inss.calcular(funcionario, null);
        assertEquals(0, esperado.compareTo(resultado),
                "INSS faixa 3 incorreto");
    }

    @Test
    void deveCalcularIRRFFaixa1() throws Exception {
        IDesconto irrf = new IRRF();
        funcionario.getFinanceiro().setSalarioBruto(BigDecimal.valueOf(2000));
        BigDecimal esperado = funcionario.getFinanceiro().getSalarioBruto().multiply(BigDecimal.ZERO);
        BigDecimal resultado = irrf.calcular(funcionario, null);
        assertEquals(0, esperado.compareTo(resultado),
                "IRRF faixa 1 incorreto");
    }

    @Test
    void deveCalcularIRRFFaixa2() throws Exception {
        IDesconto irrf = new IRRF();
        funcionario.getFinanceiro().setSalarioBruto(BigDecimal.valueOf(3000));
        BigDecimal esperado = funcionario.getFinanceiro().getSalarioBruto().multiply(new BigDecimal("0.15"));
        BigDecimal resultado = irrf.calcular(funcionario, null);
        assertEquals(0, esperado.compareTo(resultado),
                "IRRF faixa 2 incorreto");
    }

    @Test
    void deveCalcularIRRFFaixa3() throws Exception {
        IDesconto irrf = new IRRF();
        funcionario.getFinanceiro().setSalarioBruto(BigDecimal.valueOf(4000));
        BigDecimal esperado = funcionario.getFinanceiro().getSalarioBruto().multiply(new BigDecimal("0.225"));
        BigDecimal resultado = irrf.calcular(funcionario, null);
        assertEquals(0, esperado.compareTo(resultado),
                "IRRF faixa 3 incorreto");
    }

    @Test
    @DisplayName("VT: Calcula o desconto limitado ao teto de 6% do salário")
    void testeCalculoVT_Teto() throws Exception {
        IDesconto vt = new DescontoValeTransporte();
        BigDecimal salario = new BigDecimal("2500.00");
        BigDecimal valorDiario = new BigDecimal("15.00");
        YearMonth mesReferencia = YearMonth.of(2025, 10);

        funcionario.getFinanceiro().setSalarioBruto(salario);
        funcionario.getFinanceiro().setValorDiarioValeTransporte(valorDiario);

        BigDecimal esperado = salario.multiply(new BigDecimal("0.06"));

        BigDecimal resultado = vt.calcular(funcionario, mesReferencia);

        assertEquals(0, esperado.compareTo(resultado),
                "O desconto do VT deveria ser o teto de 6% do salário.");
    }

    @Test
    @DisplayName("VT: Calcula o desconto com base no custo real do benefício")
    void testeCalculoVT_CustoReal() throws Exception {
        IDesconto vt = new DescontoValeTransporte();
        BigDecimal salario = new BigDecimal("2500.00");
        BigDecimal valorDiario = new BigDecimal("5.00");
        YearMonth mesReferencia = YearMonth.of(2025, 10);

        funcionario.getFinanceiro().setSalarioBruto(salario);
        funcionario.getFinanceiro().setValorDiarioValeTransporte(valorDiario);

        BigDecimal esperado = new BigDecimal("115.00");

        BigDecimal resultado = vt.calcular(funcionario, mesReferencia);

        assertEquals(0, esperado.compareTo(resultado),
                "O desconto do VT deveria ser o custo real do benefício no mês.");
    }
}