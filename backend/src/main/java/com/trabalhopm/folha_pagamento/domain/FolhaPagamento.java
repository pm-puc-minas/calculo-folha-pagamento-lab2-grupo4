package com.trabalhopm.folha_pagamento.domain;

import com.trabalhopm.folha_pagamento.service.provento.IProvento;
import com.trabalhopm.folha_pagamento.service.desconto.IDesconto;

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

    // Agora usamos as interfaces em vez das classes inexistentes
    private List<IProvento> proventos = new ArrayList<>();
    private List<IDesconto> descontos = new ArrayList<>();

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

    // Métodos atualizados
    public void adicionarProvento(@NotNull IProvento provento) {
        proventos.add(provento);
    }

    public void adicionarDesconto(@NotNull IDesconto desconto) {
        descontos.add(desconto);
    }

    public void processarCalculos() throws Exception {

        // Total de proventos
        totalProventos = BigDecimal.ZERO;
        for (IProvento p : proventos) {
            totalProventos = totalProventos.add(p.calcular(funcionario, periodo));
        }

        // Total de descontos
        totalDescontos = BigDecimal.ZERO;
        for (IDesconto d : descontos) {
            totalDescontos = totalDescontos.add(d.calcular(funcionario.getFinanceiro().getSalarioBruto()));
        }

        // Salário líquido
        salarioLiquido = totalProventos.subtract(totalDescontos);
    }
}
