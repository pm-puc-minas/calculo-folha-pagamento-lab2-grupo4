package com.trabalhopm.folha_pagamento.domain;

import com.trabalhopm.folha_pagamento.service.desconto.*;
import com.trabalhopm.folha_pagamento.service.provento.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;

import static org.junit.jupiter.api.Assertions.*;

class FolhaPagamentoTest {

    private Funcionario funcionario;
    private FolhaPagamento folha;

    @BeforeEach
    void setup() {
        funcionario = new Funcionario();
        funcionario.setNome("Gabriel Almeida");
        funcionario.setCPF("12345678909");
        funcionario.setCargo("Desenvolvedor");
        funcionario.setDataAdmissao(LocalDate.of(2023, 1, 10));

        // Aqui criamos um objeto Financeiro fictício
        Financeiro financeiro = new Financeiro();
        financeiro.setSalarioBruto(new BigDecimal("5000.00"));
        funcionario.setFinanceiro(financeiro);

        folha = new FolhaPagamento(funcionario, YearMonth.of(2025, 10));
    }

    @Test
    void deveAdicionarProventoEDescontoCorretamente() {
        folha.adicionarProvento(new ValeAlimentacao());
        folha.adicionarDesconto(new INSS());

        assertEquals(1, folha.getProventos().size());
        assertEquals(1, folha.getDescontos().size());
    }

    @Test
    void deveCalcularFolhaComProventosEDescontos() throws Exception {
        // Arrange
        folha.adicionarProvento(new ValeAlimentacao());
        folha.adicionarDesconto(new INSS());
        folha.adicionarDesconto(new IRRF());
        folha.adicionarDesconto(new ValeTransporte());

        // Act
        folha.processarCalculos();

        // Assert
        assertNotNull(folha.getSalarioLiquido(), "Salário líquido não pode ser nulo");
        assertTrue(folha.getSalarioLiquido().compareTo(BigDecimal.ZERO) > 0, "Salário líquido deve ser positivo");
    }

    @Test
    void deveCalcularCorretamenteQuandoNaoHaProventosNemDescontos() throws Exception {
        folha.processarCalculos();

        assertEquals(BigDecimal.ZERO, folha.getTotalProventos());
        assertEquals(BigDecimal.ZERO, folha.getTotalDescontos());
        assertEquals(BigDecimal.ZERO, folha.getSalarioLiquido());
    }

    @Test
    void deveLancarErroQuandoFuncionarioForNulo() {
        assertThrows(IllegalArgumentException.class, () -> {
            FolhaPagamento folhaInvalida = new FolhaPagamento(null, YearMonth.now());
            folhaInvalida.processarCalculos();
        });
    }
}
