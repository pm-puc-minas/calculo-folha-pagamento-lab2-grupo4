package com.trabalhopm.folha_pagamento.domain;

import java.time.LocalDate;

import com.trabalhopm.folha_pagamento.utils.ValidaCPF;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Funcionario {

    @NotBlank(message = "O nome não pode estar em branco")
    private String nome;

    @NotBlank(message = "O CPF não pode estar em branco")
    private String cpf;

    @NotBlank(message = "O cargo não pode estar em branco")
    private String cargo;

    @PastOrPresent(message = "Digite uma data de admissão válida")
    private LocalDate dataAdmissao;

    @PositiveOrZero(message = "O número de dependentes não pode ser negativo")
    private byte numeroDeDependentes;

    @Min(0)
    @Max(100)
    private byte nivelInsalubridade;

    private boolean temPericulosidade;

    private Financeiro financeiro;

    public void setCPF(String cpf) {
        if(!ValidaCPF.isCPF(cpf)) {
            throw new IllegalArgumentException("CPF Invalido");
        }
        this.cpf = cpf;
    }
}