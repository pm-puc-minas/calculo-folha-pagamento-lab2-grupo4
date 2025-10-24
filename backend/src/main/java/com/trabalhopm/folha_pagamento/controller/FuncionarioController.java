package com.trabalhopm.folha_pagamento.controller;

import com.trabalhopm.folha_pagamento.domain.Financeiro;
import com.trabalhopm.folha_pagamento.domain.Funcionario;
import com.trabalhopm.folha_pagamento.service.FuncionarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/funcionarios")
public class FuncionarioController {

    @Autowired
    private FuncionarioService funcionarioService;

    @GetMapping
    public ResponseEntity<List<Funcionario>> findAll(){
        try {
            List<Funcionario> list = funcionarioService.findAll();
            return ResponseEntity.ok().body(list);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Funcionario> findById(@PathVariable Long id){
        try {
            Funcionario obj = funcionarioService.findById(id);
            return ResponseEntity.ok().body(obj);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(value = "/{id}/financeiro")
    public ResponseEntity<Financeiro> getFinanceiro(@PathVariable Long id){
        try {
            Funcionario obj = funcionarioService.findById(id);
            Financeiro financeiro = obj.getFinanceiro();

            if (financeiro == null) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.ok().body(financeiro);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
