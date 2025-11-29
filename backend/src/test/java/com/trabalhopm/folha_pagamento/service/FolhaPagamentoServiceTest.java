package com.trabalhopm.folha_pagamento.service;

import com.trabalhopm.folha_pagamento.domain.Financeiro;
import com.trabalhopm.folha_pagamento.domain.Funcionario;
import com.trabalhopm.folha_pagamento.service.desconto.DescontoFactory;
import com.trabalhopm.folha_pagamento.service.desconto.IDesconto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class FolhaPagamentoServiceTest {

    private FolhaPagamentoService service;
    private Funcionario funcionario;

    @BeforeEach
    void setup() {
        service = new FolhaPagamentoService();
        service.setDescontoFactory(new DescontoFactory()); // setter só para testes

        Financeiro fin = new Financeiro();
        fin.setSalarioBruto(new BigDecimal("2500.00"));
        fin.setHorasTrabalhadasDia(8);
        fin.setValorDiarioValeTransporte(new BigDecimal("11"));
        funcionario = new Funcionario();

        funcionario.setFinanceiro(fin);
        funcionario.setDataAdmissao(LocalDate.now());
    }

    @Test
    @DisplayName("Descontos: Deve calcular INSS, IRRF, VT e total corretamente")
    void deveCalcularDescontos() throws Exception {

        YearMonth mes = YearMonth.of(2025, 10);

        Map<String, BigDecimal> valores = service.calcularDescontos(funcionario, mes);

        assertNotNull(valores);

        // Valores individuais presentes
        assertTrue(valores.containsKey("INSS"));
        assertTrue(valores.containsKey("IRRF"));
        assertTrue(valores.containsKey("DescontoValeTransporte"));
        assertTrue(valores.containsKey("total"));

        BigDecimal somaIndividual =
                valores.get("INSS")
                        .add(valores.get("IRRF"))
                        .add(valores.get("DescontoValeTransporte"));

        assertEquals(0, somaIndividual.compareTo(valores.get("total")),
                "O total deveria ser a soma dos descontos individuais.");
    }
}
