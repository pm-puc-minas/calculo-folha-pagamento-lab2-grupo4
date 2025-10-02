package com.trabalhopm.folha_pagamento.domain;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Funcionario {
    private String nome;
    private String cpf;
    private String cargo;
    private LocalDate dataAdmissao;
    private byte numeroDeDependentes;
    private byte nivelInsalubridade;

    @Setter
    private boolean temPericulosidade;

    @Setter
    private Financeiro financeiro;

    public void setNome(String nome) {
        if (nome == null || nome.isEmpty()) {
            throw new IllegalArgumentException("O nome não pode ser vazio");
        }
        this.nome = nome;
    }

    public void setCpf(String cpf) {
        if (cpf == null || cpf.isEmpty()) {
            throw new IllegalArgumentException("O CPF não pode ser vazio");
        }
        this.cpf = cpf;
    }

    public void setCargo(String cargo) {
        if (cargo == null || cargo.isEmpty()) {
            throw new IllegalArgumentException("O cargo não pode ser vazio");
        }
        this.cargo = cargo;
    }

    public void setDataAdmissao(LocalDate dataAdmissao) {
        if (dataAdmissao == null || dataAdmissao.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("A data de admissão não é válida");
        }
        this.dataAdmissao = dataAdmissao;
    }

    public void setNumeroDeDependentes(byte numeroDeDependentes) {
        if (numeroDeDependentes < 0) {
            throw new IllegalArgumentException("Número de dependentes inválido");
        }
        this.numeroDeDependentes = numeroDeDependentes;
    }

    public void setNivelInsalubridade(byte nivelInsalubridade) {
        if (nivelInsalubridade >= 0 && nivelInsalubridade <= 100) {
            this.nivelInsalubridade = nivelInsalubridade;
        } else {
            throw new IllegalArgumentException("Nível de insalubridade inválido");
        }
    }
}
