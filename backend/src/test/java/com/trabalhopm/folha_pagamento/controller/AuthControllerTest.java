package com.trabalhopm.folha_pagamento.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trabalhopm.folha_pagamento.domain.Funcionario;
import com.trabalhopm.folha_pagamento.dto.AuthDTO;
import com.trabalhopm.folha_pagamento.repository.FuncionarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private Funcionario funcionarioExistente;

    @BeforeEach
    void setup() {
        funcionarioRepository.deleteAll();

        funcionarioExistente = new Funcionario();
        funcionarioExistente.setNome("Gabriel Almeida");
        funcionarioExistente.setCPF("12345678909");
        funcionarioExistente.setCargo("Desenvolvedor");
        funcionarioExistente.setDataAdmissao(LocalDate.now());
        funcionarioExistente.setSenha(passwordEncoder.encode("123456")); // senha criptografada

        funcionarioRepository.save(funcionarioExistente);
    }

    @Test
    void deveFazerLoginERetornarToken() throws Exception {
        AuthDTO loginRequest = new AuthDTO(funcionarioExistente.getCpf(), "123456");

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists());
    }

    @Test
    void deveNegarLoginComSenhaIncorreta() throws Exception {
        AuthDTO loginRequest = new AuthDTO(funcionarioExistente.getCpf(), "senhaErrada");

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Falha na autenticação")));
    }

    @Test
    void deveNegarLoginComUsuarioInexistente() throws Exception {
        AuthDTO loginRequest = new AuthDTO("00000000000", "123456");

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Falha na autenticação")));
    }
}
