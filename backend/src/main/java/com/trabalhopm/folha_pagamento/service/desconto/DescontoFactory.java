package com.trabalhopm.folha_pagamento.service.desconto;

import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

@Component
public class DescontoFactory {

    public List<IDesconto> criarDescontos() {
        return new ArrayList<>(List.of(
                new INSS(),
                new IRRF(),
                new DescontoValeTransporte()
        ));
    }
}