package com.trabalhopm.folha_pagamento.dto;

import com.trabalhopm.folha_pagamento.domain.enums.NivelInsalubridade;
import com.trabalhopm.folha_pagamento.domain.enums.TipoUsuario;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class FuncionarioDTO {

    @NotBlank(message = "O login não pode estar em branco")
    @Email(message = "O login deve ser um e-mail válido")
    private String login;

    @NotBlank(message = "A senha não pode estar em branco")
    private String senha;

    @NotNull(message = "O tipo do usuário não pode ser nulo")
    private TipoUsuario tipoUsuario;

    @NotBlank(message = "O nome não pode estar em branco")
    private String nome;

    @NotBlank(message = "O CPF não pode estar em branco")
    private String cpf;

    @NotBlank(message = "O telefone não pode estar em branco")
    private String telefone;

    @NotBlank(message = "O cargo não pode estar em branco")
    private String cargo;

    @Past
    private LocalDate dataNascimento;

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
