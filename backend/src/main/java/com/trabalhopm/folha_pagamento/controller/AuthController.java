package com.trabalhopm.folha_pagamento.controller;

import com.trabalhopm.folha_pagamento.domain.Funcionario;
import com.trabalhopm.folha_pagamento.dto.AuthDTO;
import com.trabalhopm.folha_pagamento.dto.LoginResponseDTO;
import com.trabalhopm.folha_pagamento.infra.security.TokenService;
import com.trabalhopm.folha_pagamento.repository.FuncionarioRepository;

import com.trabalhopm.folha_pagamento.service.events.LoginSucessoEvent;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody @Valid AuthDTO data){
        try {
            var usernamePassword = new UsernamePasswordAuthenticationToken(data.login(), data.senha());
            var auth = authenticationManager.authenticate(usernamePassword);

            var funcionario = (Funcionario) auth.getPrincipal();

            var token = tokenService.gerarToken(funcionario);

            eventPublisher.publishEvent(new LoginSucessoEvent(funcionario));

            return ResponseEntity.ok(new LoginResponseDTO(token));
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Falha na autenticação: " + e.getMessage());
        }
    }
}
