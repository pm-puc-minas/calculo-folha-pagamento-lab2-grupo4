package com.trabalhopm.folha_pagamento.service.desconto;


import java.math.BigDecimal;

public interface Desconto {
    BigDecimal calcular(BigDecimal valor);
    String getNome();
}
