package com.trabalhopm.folha_pagamento.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ValidaCPFTest {

    @Test
    @DisplayName("Testa um CPF válido")
    public void cpfValido() {
        String cpf = "19894624057";
        assertTrue(ValidaCPF.isCPF(cpf));
    }

    @Test
    @DisplayName("Testa um CPF inválido")
    public void cpfInvalido() {
        String cpf = "12345678901";
        assertFalse(ValidaCPF.isCPF(cpf));
    }

    @Test
    @DisplayName("Testa um CPF inválido com todos os digitos iguais")
    public void cpfIgualInvalido() {
        String cpf = "11111111111";
        assertFalse(ValidaCPF.isCPF(cpf));
    }
}
