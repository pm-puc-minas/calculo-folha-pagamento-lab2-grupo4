package com.trabalhopm.folha_pagamento.dto;

import jakarta.validation.constraints.Email;

public record AuthDTO(

        @Email(message = "Formato de e-mail inválido.")
        String login,

        String senha
) {
}
