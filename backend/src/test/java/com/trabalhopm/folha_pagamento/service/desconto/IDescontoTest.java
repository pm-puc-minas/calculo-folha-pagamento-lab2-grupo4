package com.trabalhopm.folha_pagamento.service.desconto;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

class IDescontoTest {

    @Test
    void deveCalcularINSSFaixa1() throws Exception {
        IDesconto inss = new INSS();
        BigDecimal salario = new BigDecimal("1200.00");
        BigDecimal esperado = salario.multiply(new BigDecimal("0.075"));
        BigDecimal resultado = inss.calcular(salario);
        assertEquals(0, esperado.compareTo(resultado),
                "INSS faixa 1 incorreto");
    }

    @Test
    void deveCalcularINSSFaixa2() throws Exception {
        IDesconto inss = new INSS();
        BigDecimal salario = new BigDecimal("2000.00");
        BigDecimal esperado = salario.multiply(new BigDecimal("0.09"));
        BigDecimal resultado = inss.calcular(salario);
        assertEquals(0, esperado.compareTo(resultado),
                "INSS faixa 2 incorreto");
    }

    @Test
    void deveCalcularINSSFaixa3() throws Exception {
        IDesconto inss = new INSS();
        BigDecimal salario = new BigDecimal("4000.00");
        BigDecimal esperado = salario.multiply(new BigDecimal("0.14"));
        BigDecimal resultado = inss.calcular(salario);
        assertEquals(0, esperado.compareTo(resultado),
                "INSS faixa 3 incorreto");
    }

    @Test
    void deveCalcularIRRFFaixa1() throws Exception {
        IDesconto irrf = new IRRF();
        BigDecimal salario = new BigDecimal("2000.00");
        BigDecimal esperado = salario.multiply(BigDecimal.ZERO);
        BigDecimal resultado = irrf.calcular(salario);
        assertEquals(0, esperado.compareTo(resultado),
                "IRRF faixa 1 incorreto");
    }

    @Test
    void deveCalcularIRRFFaixa2() throws Exception {
        IDesconto irrf = new IRRF();
        BigDecimal salario = new BigDecimal("3000.00");
        BigDecimal esperado = salario.multiply(new BigDecimal("0.15"));
        BigDecimal resultado = irrf.calcular(salario);
        assertEquals(0, esperado.compareTo(resultado),
                "IRRF faixa 2 incorreto");
    }

    @Test
    void deveCalcularIRRFFaixa3() throws Exception {
        IDesconto irrf = new IRRF();
        BigDecimal salario = new BigDecimal("4000.00");
        BigDecimal esperado = salario.multiply(new BigDecimal("0.225"));
        BigDecimal resultado = irrf.calcular(salario);
        assertEquals(0, esperado.compareTo(resultado),
                "IRRF faixa 3 incorreto");
    }

    @Test
    void deveCalcularValeTransporteCorretamente() throws Exception {
        IDesconto vt = new ValeTransporte();
        BigDecimal salario = new BigDecimal("2500.00");
        BigDecimal esperado = salario.multiply(new BigDecimal("0.06"));
        BigDecimal resultado = vt.calcular(salario);
        assertEquals(0, esperado.compareTo(resultado),
                "Vale transporte incorreto");
    }
}
