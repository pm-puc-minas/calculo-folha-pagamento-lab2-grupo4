package com.trabalhopm.folha_pagamento.domain;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "financeiro")
public class Financeiro implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull(message = "O salário bruto não pode ser nulo")
    @PositiveOrZero(message = "O salário bruto deve ser maior que zero")
    @Column(name = "salario_bruto")
    private BigDecimal salarioBruto;

    @PositiveOrZero(message = "As horas trabalhadas por dia devem ser maiores que zero")
    @Column(name = "horas_trabalhadas_dia")
    private double horasTrabalhadasDia;

    @Min(value = 0, message = "Os dias trabalhados na semana devem ser entre 0 e 7")
    @Max(value = 7, message = "Os dias trabalhados na semana devem ser entre 0 e 7")
    @Column(name = "dias_trabalhados_semana")
    private byte diasTrabalhadosSemana;

    @PositiveOrZero(message = "O valor diário do vale transporte não pode ser negativo")
    @Column(name = "valor_diario_VT")
    private BigDecimal valorDiarioValeTransporte;

    @PositiveOrZero(message = "O valor diário do vale alimentação não pode ser negativo")
    @Column(name = "valor_diario_VA")
    private BigDecimal valorDiarioValeAlimentacao;

    @OneToOne
    @JoinColumn(name = "funcionario_id", unique = true)
    @JsonBackReference
    private Funcionario funcionario;
}