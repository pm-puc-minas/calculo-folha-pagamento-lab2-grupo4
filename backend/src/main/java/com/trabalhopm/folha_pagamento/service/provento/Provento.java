package com.trabalhopm.folha_pagamento.service.provento;

import com.trabalhopm.folha_pagamento.domain.Funcionario;

import java.math.BigDecimal;

public interface Provento {
    BigDecimal calcular(Funcionario funcionario);
    String getNome();
}