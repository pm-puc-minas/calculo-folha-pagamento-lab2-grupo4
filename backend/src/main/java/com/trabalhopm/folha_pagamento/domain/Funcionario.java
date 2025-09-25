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
    private byte numeroDeDepententes;
    private byte nivelInsalubridade;

    @Setter
    private boolean temPericulosidade;

    @Setter
    private Financeiro financeiro;

    public void setNome(String nome) {
        if(nome == null || nome.isEmpty()){
            throw new IllegalArgumentException("O nome não pode ser vazio");
        }
        this.nome = nome;
    }

    public void setCpf(String cpf) {
        //TODO! colocar logica para validar cpf :D
        if(cpf == null || cpf.isEmpty()){
            throw new IllegalArgumentException("O CPF não pode ser vazio");
        }
        this.cpf = cpf;
    }

    public void setCargo(String cargo) {
        if(cargo == null || cargo.isEmpty()){
            throw new IllegalArgumentException("O cargo não pode ser vazio");
        }
        this.cargo = cargo;
    }

    public void setDataAdmissao(LocalDate dataAdmissao) {
        if(dataAdmissao == null || dataAdmissao.isAfter(LocalDate.now())){
            throw new IllegalArgumentException("A data de admissão não é válida");
        }
        this.dataAdmissao = dataAdmissao;
    }

    public void setNumeroDeDepententes(byte numeroDeDepententes) {
        if(numeroDeDepententes < 0){
            throw new IllegalArgumentException("A quantidade de dependentes é inválida");
        }
        this.numeroDeDepententes = numeroDeDepententes;
    }

    public void setNivelInsalubridade(byte nivelInsalubridade) {
        if(nivelInsalubridade >= 0 && nivelInsalubridade <= 100){
            this.nivelInsalubridade = nivelInsalubridade;
        }
        else{
            throw new IllegalArgumentException("O nível de insalubridade é inválido");
        }
    }
}
