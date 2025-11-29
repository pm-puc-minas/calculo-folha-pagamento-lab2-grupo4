package com.trabalhopm.folha_pagamento.service.desconto;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DescontoFactory {

    public List<IDesconto> getDescontos() {
        List<IDesconto> lista = new ArrayList<>();

        lista.add(new INSS());
        lista.add(new IRRF());
        lista.add(new DescontoValeTransporte());

        return lista;
    }
}
