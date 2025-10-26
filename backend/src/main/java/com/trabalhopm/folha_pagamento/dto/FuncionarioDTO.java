package com.trabalhopm.folha_pagamento.dto;

import com.trabalhopm.folha_pagamento.domain.enums.NivelInsalubridade;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

import java.time.LocalDate;

@Data
public class FuncionarioDTO {

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

    @NotNull
    private NivelInsalubridade nivelInsalubridade;

    private boolean temPericulosidade;

    private boolean deFerias;

    @NotNull(message = "Os dados financeiros não podem ser nulos")
    private FinanceiroDTO financeiro;
}
