package com.trabalhopm.folha_pagamento.domain;

import lombok.Setter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;


import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FolhaPagamento {

    @NotNull(message = "O funcionário não pode ser nulo")
    private Funcionario funcionario;

    @NotNull(message = "O período da folha não pode ser nulo")
    private YearMonth periodo;

    private List<Provento> proventos = new ArrayList<>();
    private List<Desconto> descontos = new ArrayList<>();

    @PositiveOrZero(message = "O valor do salário/hora não pode ser negativo")
    private BigDecimal valorSalarioHora = BigDecimal.ZERO;

    @PositiveOrZero(message = "A base de cálculo do INSS não pode ser negativa")
    private BigDecimal baseCalculoINSS = BigDecimal.ZERO;

    @PositiveOrZero(message = "A base de cálculo do FGTS não pode ser negativa")
    private BigDecimal baseCalculoFGTS = BigDecimal.ZERO;

    @PositiveOrZero(message = "A base de cálculo do IRRF não pode ser negativa")
    private BigDecimal baseCalculoIRRF = BigDecimal.ZERO;

    @PositiveOrZero(message = "O total de proventos não pode ser negativo")
    private BigDecimal totalProventos = BigDecimal.ZERO;

    @PositiveOrZero(message = "O total de descontos não pode ser negativo")
    private BigDecimal totalDescontos = BigDecimal.ZERO;

    @PositiveOrZero(message = "O salário líquido não pode ser negativo")
    private BigDecimal salarioLiquido = BigDecimal.ZERO;

    @PositiveOrZero(message = "O valor do INSS não pode ser negativo")
    private BigDecimal valorINSS = BigDecimal.ZERO;

    @PositiveOrZero(message = "O valor do IRRF não pode ser negativo")
    private BigDecimal valorIRRF = BigDecimal.ZERO;

    @PositiveOrZero(message = "O valor do FGTS não pode ser negativo")
    private BigDecimal valorFGTS = BigDecimal.ZERO;

     
    public FolhaPagamento(@NotNull Funcionario funcionario, @NotNull YearMonth periodo) {
        this.funcionario = funcionario;
        this.periodo = periodo;
    }

    
    public void adicionarProvento(@NotNull Provento provento) {
        proventos.add(provento);
    }

    public void adicionarDesconto(@NotNull Desconto desconto) {
        descontos.add(desconto);
    }

    public void processarCalculos() {
        
        totalProventos = proventos.stream()
                .map(Provento::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        totalDescontos = descontos.stream()
                .map(Desconto::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        salarioLiquido = totalProventos.subtract(totalDescontos);

        // Regras reais de INSS, IRRF, FGTS 
    }
}
