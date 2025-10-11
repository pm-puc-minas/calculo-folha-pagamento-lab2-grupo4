package com.trabalhopm.folha_pagamento.service.provento;

import java.math.BigDecimal;

import com.trabalhopm.folha_pagamento.domain.Funcionario;

public interface Provento {
    BigDecimal calcular(Funcionario funcionario);
    String getNome();
    BigDecimal getValor();
}