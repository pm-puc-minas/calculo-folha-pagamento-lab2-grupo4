package com.trabalhopm.folha_pagamento.service.desconto;


import java.math.BigDecimal;

public interface IDesconto {
    BigDecimal calcular(BigDecimal valor) throws Exception;
    String getNome();
}
