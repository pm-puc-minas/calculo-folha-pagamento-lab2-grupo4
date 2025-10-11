package com.trabalhopm.folha_pagamento.service.desconto;

import com.trabalhopm.folha_pagamento.domain.Funcionario;

import java.math.BigDecimal;
import java.time.YearMonth;

public interface IDesconto {
    BigDecimal calcular(Funcionario funcionario, YearMonth mesReferencia) throws Exception;
    String getNome();
}
