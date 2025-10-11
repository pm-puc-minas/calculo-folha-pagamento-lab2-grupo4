package com.trabalhopm.folha_pagamento.domain;
import lombok.Setter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Financeiro {
    
    @NotNull(message = "O salário bruto não pode ser nulo")
    @PositiveOrZero(message = "O salário bruto deve ser maior que zero")
    private BigDecimal salarioBruto;

    @PositiveOrZero(message = "As horas trabalhadas por dia devem ser maiores que zero")
    private double horasTrabalhadasDia;

    @Min(value = 0, message = "Os dias trabalhados na semana devem ser entre 0 e 7")
    @Max(value = 7, message = "Os dias trabalhados na semana devem ser entre 0 e 7")
    private byte diasTrabalhadosSemana;

    @PositiveOrZero(message = "O valor diário do vale transporte não pode ser negativo")
    private BigDecimal valorDiarioValeTransporte;
}