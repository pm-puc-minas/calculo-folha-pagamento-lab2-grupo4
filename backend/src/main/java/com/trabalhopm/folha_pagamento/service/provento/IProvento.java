package com.trabalhopm.folha_pagamento.service.provento;

import com.trabalhopm.folha_pagamento.domain.Funcionario;
import java.math.BigDecimal;
import java.time.YearMonth;

public interface IProvento {
    BigDecimal calcular(Funcionario funcionario, YearMonth mesReferencia) throws Exception;
    String getNome();
}
