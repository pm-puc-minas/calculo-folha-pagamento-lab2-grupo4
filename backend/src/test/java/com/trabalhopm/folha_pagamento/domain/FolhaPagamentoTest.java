package com.trabalhopm.folha_pagamento.domain;

import com.trabalhopm.folha_pagamento.repository.FolhaPagamentoRepository;
import com.trabalhopm.folha_pagamento.service.FolhaPagamentoService;
import com.trabalhopm.folha_pagamento.service.desconto.*;
import com.trabalhopm.folha_pagamento.service.provento.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FolhaPagamentoTest {

    @Spy
    @InjectMocks
    private FolhaPagamentoService folhaPagamentoService;

    @Mock
    private FolhaPagamentoRepository folhaPagamentoRepository;

    private Funcionario funcionario;
    private YearMonth mesReferencia;

    @BeforeEach
    void setup() {
        funcionario = new Funcionario();
        funcionario.setNome("Gabriel Almeida");
        funcionario.setCPF("19894624057");
        funcionario.setCargo("Desenvolvedor");
        funcionario.setDataAdmissao(LocalDate.of(2023, 1, 10));

        Financeiro financeiro = new Financeiro();
        financeiro.setSalarioBruto(new BigDecimal("5000.00"));
        funcionario.setFinanceiro(financeiro);

        mesReferencia = YearMonth.of(2025, 10);
    }

    @Test
    @DisplayName("Deve gerar folha com proventos e descontos (usando Service)")
    void deveCalcularFolhaComProventosEDescontos() throws Exception {
        // Arrange

        HashMap<String, BigDecimal> proventosMock = new HashMap<>();
        proventosMock.put("total", new BigDecimal("200.00"));
        proventosMock.put("SalarioFamilia", new BigDecimal("50.00"));
        proventosMock.put("Ferias", new BigDecimal("150.00"));
        proventosMock.put("ValeAlimentacao", new BigDecimal("300.00"));
        proventosMock.put("ProventoValeTransporte", new BigDecimal("100.00"));

        HashMap<String, BigDecimal> descontosMock = new HashMap<>();
        descontosMock.put("total", new BigDecimal("700.00"));
        descontosMock.put("INSS", new BigDecimal("500.00"));
        descontosMock.put("IRRF", new BigDecimal("200.00"));

        HashMap<String, BigDecimal> encargosMock = new HashMap<>();
        encargosMock.put("FGTS", new BigDecimal("400.00"));

        doReturn(proventosMock).when(folhaPagamentoService).calcularProventos(any(Funcionario.class), any(YearMonth.class));
        doReturn(descontosMock).when(folhaPagamentoService).calcularDescontos(any(Funcionario.class), any(YearMonth.class));
        doReturn(encargosMock).when(folhaPagamentoService).calcularEncargoSocial(any(Funcionario.class));

        BigDecimal salarioLiquidoCalculado = new BigDecimal("4500.00");
        doReturn(salarioLiquidoCalculado)
                .when(folhaPagamentoService)
                .calcularSalarioLiquido(funcionario, new BigDecimal("700.00"), new BigDecimal("200.00"));


        when(folhaPagamentoRepository.findByFuncionarioAndPeriodo(funcionario, mesReferencia)).thenReturn(Optional.empty());
        when(folhaPagamentoRepository.save(any(FolhaPagamento.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        FolhaPagamento folhaGerada = folhaPagamentoService.getFolha(funcionario, mesReferencia);

        // Assert
        assertNotNull(folhaGerada, "A folha gerada não pode ser nula");

        assertEquals(new BigDecimal("500.00"), folhaGerada.getValorINSS());
        assertEquals(new BigDecimal("200.00"), folhaGerada.getValorIRRF());
        assertEquals(new BigDecimal("50.00"), folhaGerada.getSalarioFamilia());
        assertEquals(new BigDecimal("150.00"), folhaGerada.getAdicionalFerias());
        assertEquals(new BigDecimal("300.00"), folhaGerada.getValeAlimentacao());
        assertEquals(new BigDecimal("100.00"), folhaGerada.getProventoValeTransporte());
        assertEquals(new BigDecimal("400.00"), folhaGerada.getValorFGTS());

        assertEquals(salarioLiquidoCalculado, folhaGerada.getSalarioLiquido(), "Salário líquido está incorreto");
    }

    @Test
    @DisplayName("Deve calcular folha zerada (usando Service) quando não há proventos/descontos")
    void deveCalcularCorretamenteQuandoNaoHaProventosNemDescontos() throws Exception {
        HashMap<String, BigDecimal> proventosMock = new HashMap<>();
        proventosMock.put("total", BigDecimal.ZERO);
        HashMap<String, BigDecimal> descontosMock = new HashMap<>();
        descontosMock.put("total", BigDecimal.ZERO);
        HashMap<String, BigDecimal> encargosMock = new HashMap<>();

        doReturn(proventosMock).when(folhaPagamentoService).calcularProventos(any(), any());
        doReturn(descontosMock).when(folhaPagamentoService).calcularDescontos(any(), any());
        doReturn(encargosMock).when(folhaPagamentoService).calcularEncargoSocial(any());

        BigDecimal salarioLiquidoCalculado = new BigDecimal("5000.00");
        doReturn(salarioLiquidoCalculado)
                .when(folhaPagamentoService)
                .calcularSalarioLiquido(funcionario, BigDecimal.ZERO, BigDecimal.ZERO);

        when(folhaPagamentoRepository.findByFuncionarioAndPeriodo(funcionario, mesReferencia)).thenReturn(Optional.empty());
        when(folhaPagamentoRepository.save(any(FolhaPagamento.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        FolhaPagamento folhaGerada = folhaPagamentoService.getFolha(funcionario, mesReferencia);

        // Assert
        assertNotNull(folhaGerada);
        assertEquals(BigDecimal.ZERO, folhaGerada.getValorINSS());
        assertEquals(BigDecimal.ZERO, folhaGerada.getAdicionalFerias());
        assertEquals(BigDecimal.ZERO, folhaGerada.getValorFGTS());

        assertEquals(salarioLiquidoCalculado, folhaGerada.getSalarioLiquido());
    }

    @Test
    @DisplayName("Deve falhar ao tentar gerar folha para funcionário nulo (usando Service)")
    void deveLancarErroQuandoFuncionarioForNulo() {
        assertThrows(Exception.class, () -> {
            folhaPagamentoService.getFolha(null, mesReferencia);
        });
    }
}
