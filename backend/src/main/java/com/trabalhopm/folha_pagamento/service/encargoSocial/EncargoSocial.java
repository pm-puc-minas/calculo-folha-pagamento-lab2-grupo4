package com.trabalhopm.folha_pagamento.service.encargoSocial;

import com.trabalhopm.folha_pagamento.domain.Funcionario;

import java.math.BigDecimal;

public interface EncargoSocial {
    BigDecimal calcular(Funcionario funcionario);
    String getNome();
}