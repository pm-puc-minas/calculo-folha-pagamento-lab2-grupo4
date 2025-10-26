package com.trabalhopm.folha_pagamento.domain;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.YearMonth;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "folha_pagamento")
public class FolhaPagamento implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "O funcionário não pode ser nulo")
    @ManyToOne
    @JoinColumn(name = "funcionario_id")
    private Funcionario funcionario;

    @NotNull(message = "O período da folha não pode ser nulo")
    @Column(columnDefinition = "DATE")
    private YearMonth periodo;

    @PositiveOrZero(message = "O valor do salário/hora não pode ser negativo")
    @Column(name = "valor_salario_hora")
    private BigDecimal valorSalarioHora = BigDecimal.ZERO;

    @PositiveOrZero(message = "O salário líquido não pode ser negativo")
    @Column(name = "salario_liquido")
    private BigDecimal salarioLiquido = BigDecimal.ZERO;

    @PositiveOrZero(message = "O total de proventos não pode ser negativo")
    @Column(name = "total_proventos")
    private BigDecimal totalProventos = BigDecimal.ZERO;

    @PositiveOrZero(message = "O total de descontos não pode ser negativo")
    @Column(name = "total_descontos")
    private BigDecimal totalDescontos = BigDecimal.ZERO;

    @PositiveOrZero(message = "O valor do INSS não pode ser negativo")
    @Column(name = "valor_inss")
    private BigDecimal valorINSS = BigDecimal.ZERO;

    @PositiveOrZero(message = "O valor do IRRF não pode ser negativo")
    @Column(name = "valor_irrf")
    private BigDecimal valorIRRF = BigDecimal.ZERO;

    @PositiveOrZero(message = "O valor do Desconto Vale Transporte não pode ser negativo")
    @Column(name = "valor_desconto_VT")
    private BigDecimal descontoValeTransporte = BigDecimal.ZERO;

    @PositiveOrZero(message = "O valor do FGTS não pode ser negativo")
    @Column(name = "valor_fgts")
    private BigDecimal valorFGTS = BigDecimal.ZERO;

    @PositiveOrZero(message = "O valor do adicional de Insalubridade não pode ser negativo")
    @Column(name = "adicional_insalubridade")
    private BigDecimal insalubridade = BigDecimal.ZERO;

    @PositiveOrZero(message = "O valor do adicional de Periculosidade não pode ser negativo")
    @Column(name = "adicional_periculosidade")
    private BigDecimal periculosidade = BigDecimal.ZERO;

    @PositiveOrZero(message = "O valor do Provento Vale Transporte não pode ser negativo")
    @Column(name = "valor_provento_VT")
    private BigDecimal proventoValeTransporte = BigDecimal.ZERO;

    @PositiveOrZero(message = "O valor do Vale Alimentacao não pode ser negativo")
    @Column(name = "valor_VA")
    private BigDecimal valeAlimentacao = BigDecimal.ZERO;

    @PositiveOrZero(message = "O valor do Salario Familia não pode ser negativo")
    @Column(name = "salario_familia")
    private BigDecimal salarioFamilia = BigDecimal.ZERO;

    @PositiveOrZero(message = "O valor de Adicional de Ferias não pode ser negativo")
    @Column(name = "adicional_ferias")
    private BigDecimal adicionalFerias = BigDecimal.ZERO;


    public FolhaPagamento(@NotNull Funcionario funcionario, @NotNull YearMonth periodo) {
        this.funcionario = funcionario;
        this.periodo = periodo;
    }
}
