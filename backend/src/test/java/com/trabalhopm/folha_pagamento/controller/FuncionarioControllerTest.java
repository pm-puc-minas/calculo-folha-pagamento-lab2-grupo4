package com.trabalhopm.folha_pagamento.controller;

import com.trabalhopm.folha_pagamento.domain.Financeiro;
import com.trabalhopm.folha_pagamento.domain.FolhaPagamento;
import com.trabalhopm.folha_pagamento.domain.Funcionario;
import com.trabalhopm.folha_pagamento.dto.FuncionarioDTO;
import com.trabalhopm.folha_pagamento.service.FolhaPagamentoService;
import com.trabalhopm.folha_pagamento.service.FuncionarioService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.YearMonth;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class FuncionarioControllerTest {

    @InjectMocks
    private FuncionarioController funcionarioController;

    @Mock
    private FuncionarioService funcionarioService;

    @Mock
    private FolhaPagamentoService folhaPagamentoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveCriarFuncionarioComSucesso() {

        FuncionarioDTO dto = new FuncionarioDTO();
        Funcionario funcionario = new Funcionario();
        funcionario.setId(1L);
        when(funcionarioService.create(any(FuncionarioDTO.class))).thenReturn(funcionario);

        ResponseEntity<?> response = funcionarioController.createFuncionario(dto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof Funcionario);
    }

    @Test
    void deveLancarErroDeCriacaoQuandoDadosInvalidos() {
       
        FuncionarioDTO dto = new FuncionarioDTO();
        when(funcionarioService.create(any(FuncionarioDTO.class)))
                .thenThrow(new IllegalArgumentException("Dados inválidos"));

        
        ResponseEntity<?> response = funcionarioController.createFuncionario(dto);

       
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Dados inválidos", response.getBody());
    }

    @Test
    void deveGerarFolhaPagamentoComSucesso() throws Exception {
      
        Long id = 1L;
        YearMonth periodo = YearMonth.now();
        Funcionario funcionario = new Funcionario();
        FolhaPagamento folha = new FolhaPagamento();
        
        when(funcionarioService.findById(id)).thenReturn(funcionario);
        when(folhaPagamentoService.getFolha(any(Funcionario.class), any(YearMonth.class))).thenReturn(folha).thenThrow(new RuntimeException("Erro ao gerar folha"));

        ResponseEntity<?> response = funcionarioController.gerarFolhaPagamento(id, periodo);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof FolhaPagamento);
    }

    @Test
    void deveListarTodosFuncionarios() {
       
        List<Funcionario> funcionarios = Arrays.asList(new Funcionario(), new Funcionario());
        when(funcionarioService.findAll()).thenReturn(funcionarios);

        ResponseEntity<List<Funcionario>> response = funcionarioController.findAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void deveBuscarFuncionarioPorId() {
      
        Long id = 1L;
        Funcionario funcionario = new Funcionario();
        when(funcionarioService.findById(id)).thenReturn(funcionario);

        ResponseEntity<Funcionario> response = funcionarioController.findById(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void deveRetornarErroQuandoFuncionarioNaoEncontrado() {
       
        Long id = 1L;
        when(funcionarioService.findById(id)).thenThrow(new EntityNotFoundException());

        ResponseEntity<Funcionario> response = funcionarioController.findById(id);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void deveBuscarFinanceiroDoFuncionario() {
       
        Long id = 1L;
        Funcionario funcionario = new Funcionario();
        Financeiro financeiro = new Financeiro();
        funcionario.setFinanceiro(financeiro);
        when(funcionarioService.findById(id)).thenReturn(funcionario);

        ResponseEntity<Financeiro> response = funcionarioController.getFinanceiro(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void deveListarFolhasPagamento() {

        Long id = 1L;
        Funcionario funcionario = new Funcionario();
        List<FolhaPagamento> folhas = Arrays.asList(new FolhaPagamento(), new FolhaPagamento());
        
        when(funcionarioService.findById(id)).thenReturn(funcionario);
        when(folhaPagamentoService.findAllByFuncionario(funcionario)).thenReturn(folhas);

        ResponseEntity<List<FolhaPagamento>> response = funcionarioController.getFolhas(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void deveDeletarFolhaPagamentoComSucesso() {
        
        Long id = 1L;
        YearMonth periodo = YearMonth.now();
        Funcionario funcionario = new Funcionario();
        when(funcionarioService.findById(id)).thenReturn(funcionario);
        doNothing().when(folhaPagamentoService).deleteByFuncionarioAndPeriodo(any(Funcionario.class), any(YearMonth.class));

        ResponseEntity<?> response = funcionarioController.deleteFolhaPagamento(id, periodo);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void deveLancarErroAoDeletarFolhaPagamentoQuandoFuncionarioNaoEncontrado() {
        
        Long id = 1L;
        YearMonth periodo = YearMonth.now();
        when(funcionarioService.findById(id)).thenThrow(new EntityNotFoundException("Funcionário não encontrado"));

        ResponseEntity<?> response = funcionarioController.deleteFolhaPagamento(id, periodo);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}