package com.trabalhopm.folha_pagamento.service.desconto;

import java.math.BigDecimal;
import java.math.RoundingMode;

import com.trabalhopm.folha_pagamento.domain.Funcionario;
import com.trabalhopm.folha_pagamento.service.provento.Provento;

public class ProventoValeTransporte implements Provento {

    private BigDecimal valorProvento = BigDecimal.ZERO; 

    @Override
    public BigDecimal calcular(Funcionario funcionario) {
        
        
        BigDecimal custoTotalVT = getCustoTotalVT(
            // TO DO: passar os parametros necessarios para calcuar o custo total do vale transporte baseado em
            //funcionario
            );
        
        
        ValeTransporte calculoDescontoVT = new ValeTransporte();
        BigDecimal descontoFuncionario = calculoDescontoVT.calcular(funcionario.getFinanceiro().getSalarioBruto());
        
        
        BigDecimal proventoEmpresa = custoTotalVT.subtract(descontoFuncionario);
        
        this.valorProvento = proventoEmpresa.setScale(2, RoundingMode.HALF_UP);
        return this.valorProvento;
    }
    
    
    private BigDecimal getCustoTotalVT(
        // TO DO: receber os parametros necessarios para calcuar o custo total do vale transporte baseado em funcionario
        // Funcionario funcionario
        ) {
        return new BigDecimal("300.00"); 
    }

    @Override
    public String getNome() {
        return "Provento Vale-Transporte (Custo Empresa)";
    }
    
  
    @Override
    public BigDecimal getValor() {
        return this.valorProvento;   

    }
}