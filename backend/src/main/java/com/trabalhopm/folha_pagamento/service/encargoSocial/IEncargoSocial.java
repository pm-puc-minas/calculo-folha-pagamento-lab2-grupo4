package com.trabalhopm.folha_pagamento.service.encargoSocial;

import java.math.BigDecimal;

import com.trabalhopm.folha_pagamento.domain.Funcionario;

public interface IEncargoSocial {
    BigDecimal calcular(Funcionario funcionario);
    String getNome();
}