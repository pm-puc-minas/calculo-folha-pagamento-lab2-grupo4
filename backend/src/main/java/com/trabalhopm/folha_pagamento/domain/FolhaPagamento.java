package com.trabalhopm.folha_pagamento.domain;

import com.trabalhopm.folha_pagamento.service.encargoSocial.IEncargoSocial;
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

    private List<IProvento> proventos = new ArrayList<>();
    private List<IDesconto> descontos = new ArrayList<>();
    private List<IEncargoSocial> encargos = new ArrayList<>();

    @PositiveOrZero(message = "O valor do salário/hora não pode ser negativo")
    private BigDecimal valorSalarioHora = BigDecimal.ZERO;

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

    @PositiveOrZero(message = "O valor do Vale Transporte não pode ser negativo")
    private BigDecimal valeTransporte = BigDecimal.ZERO;

    @PositiveOrZero(message = "O valor do Vale Alimentacao não pode ser negativo")
    private BigDecimal valeAlimentacao = BigDecimal.ZERO;

    @PositiveOrZero(message = "O valor do Salario Familia não pode ser negativo")
    private BigDecimal salarioFamilia = BigDecimal.ZERO;

    @PositiveOrZero(message = "O valor de Adicional de Ferias não pode ser negativo")
    private BigDecimal adicionalFerias = BigDecimal.ZERO;


    public FolhaPagamento(@NotNull Funcionario funcionario, @NotNull YearMonth periodo) {
        this.funcionario = funcionario;
        this.periodo = periodo;
    }

    public void adicionarProvento(@NotNull IProvento provento) {
        proventos.add(provento);
    }

    public void adicionarDesconto(@NotNull IDesconto desconto) {
        descontos.add(desconto);
    }

    public void processarCalculos() throws Exception {

        totalProventos = BigDecimal.ZERO;
        for (IProvento p : proventos) {
            BigDecimal valorProvento = p.calcular(funcionario, periodo);
            totalProventos = totalProventos.add(valorProvento);

            String nomeProvento = p.getNome();

            if (nomeProvento.equals("SalarioFamilia")) {
                salarioFamilia = valorProvento;
            }
            else if (nomeProvento.equals("Ferias")) {
                adicionalFerias = valorProvento;
            }
            else if (nomeProvento.equals("ValeAlimentacao")) {
                valeAlimentacao = valorProvento;
            }
            else if (nomeProvento.equals("ValeTransporte")) {
                valeTransporte =  valorProvento;
            }
        }

        totalDescontos = BigDecimal.ZERO;
        for (IDesconto d : descontos) {
            BigDecimal valorDesconto = d.calcular(funcionario, periodo);
            totalDescontos = totalDescontos.add(valorDesconto);

            String nomeDesconto = d.getNome();

            if (nomeDesconto.equals("INSS")) {
                this.setValorINSS(valorDesconto);
            }
            else if (nomeDesconto.equals("IRRF")) {
                this.setValorIRRF(valorDesconto);
            }
        }

        for (IEncargoSocial e : encargos) {
            BigDecimal valorEncargo = e.calcular(funcionario);

            String nomeEncargo = e.getNome();

            if (nomeEncargo.equals("FGTS")) {
                this.setValorFGTS(valorEncargo);
            }
        }

        salarioLiquido = totalProventos.subtract(totalDescontos);
    }
}
