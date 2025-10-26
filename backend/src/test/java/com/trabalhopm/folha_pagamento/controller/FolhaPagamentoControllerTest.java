package com.trabalhopm.folha_pagamento.controller;

import com.trabalhopm.folha_pagamento.domain.FolhaPagamento;
import com.trabalhopm.folha_pagamento.service.FolhaPagamentoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(MockitoExtension.class)
class FolhaPagamentoControllerTest {

    
    @InjectMocks
    private FolhaPagamentoController folhaPagamentoController;

    
    @Mock
    private FolhaPagamentoService folhaPagamentoService;

   
    private MockMvc mockMvc;

    private FolhaPagamento folha1;
    private FolhaPagamento folha2;

    @BeforeEach
    void setUp() {
        
        MockitoAnnotations.openMocks(this);
        
        
        mockMvc = MockMvcBuilders.standaloneSetup(folhaPagamentoController).build();

        
        folha1 = new FolhaPagamento();
        folha1.setId(1L);
        
        folha2 = new FolhaPagamento();
        folha2.setId(2L);
    }

    //testes para o endpoint GET /folhas (findAll)

    @Test
    @DisplayName("Deve listar todas as folhas de pagamento com sucesso e retornar 200 OK")
    void deveListarTodasAsFolhasComSucesso() throws Exception {
        
        List<FolhaPagamento> folhas = Arrays.asList(folha1, folha2);
        when(folhaPagamentoService.findAll()).thenReturn(folhas);

        
        mockMvc.perform(get("/folhas")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) 
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2))) 
                .andExpect(jsonPath("$[0].id").value(1)); 
    }

    @Test
    @DisplayName("Deve retornar 200 OK e uma lista vazia quando não houver folhas")
    void deveRetornarListaVazia() throws Exception {
        
        when(folhaPagamentoService.findAll()).thenReturn(Collections.emptyList());

        
        mockMvc.perform(get("/folhas")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0))); 
    }

    @Test
    @DisplayName("Deve retornar 404 Not Found em caso de erro no serviço findAll")
    void deveRetornar404QuandoErroNoFindAll() throws Exception {
        
        when(folhaPagamentoService.findAll()).thenThrow(new RuntimeException("Simulação de falha de DB"));

       
        mockMvc.perform(get("/folhas")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound()); 
    }

    // testes para o endpoint GET /folhas/{id} (findById)

    @Test
    @DisplayName("Deve buscar uma folha por ID com sucesso e retornar 200 OK")
    void deveBuscarFolhaPorIdComSucesso() throws Exception {
        Long id = 1L;
        
        when(folhaPagamentoService.findById(id)).thenReturn(folha1);

        
        mockMvc.perform(get("/folhas/{id}", id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) 
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(id)); 
    }

    @Test
    @DisplayName("Deve retornar 404 Not Found quando a folha não for encontrada por ID")
    void deveRetornar404QuandoFolhaNaoEncontradaPorId() throws Exception {
        Long id = 99L;
        
        when(folhaPagamentoService.findById(anyLong())).thenThrow(new RuntimeException("Folha não existe"));

        
        mockMvc.perform(get("/folhas/{id}", id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound()); 
    }
}
