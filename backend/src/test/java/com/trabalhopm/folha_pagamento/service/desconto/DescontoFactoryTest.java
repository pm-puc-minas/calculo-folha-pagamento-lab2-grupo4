package com.trabalhopm.folha_pagamento.service.desconto;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DescontoFactoryTest {

    @Test
    void deveRetornarListaDeDescontos() {
        DescontoFactory factory = new DescontoFactory();

        List<IDesconto> descontos = factory.criarDescontos();

        assertNotNull(descontos, "A lista de descontos não deveria ser nula.");
        assertEquals(3, descontos.size(), "A lista deveria conter exatamente 3 descontos.");

        assertTrue(descontos.stream().anyMatch(d -> d instanceof INSS),
                "Deveria conter INSS");
        assertTrue(descontos.stream().anyMatch(d -> d instanceof IRRF),
                "Deveria conter IRRF");
        assertTrue(descontos.stream().anyMatch(d -> d instanceof DescontoValeTransporte),
                "Deveria conter DescontoValeTransporte");
    }
}
