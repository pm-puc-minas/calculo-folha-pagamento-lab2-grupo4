package com.trabalhopm.folha_pagamento.main;
import com.trabalhopm.folha_pagamento.service.CalculadoraSalario;
import com.trabalhopm.folha_pagamento.service.desconto.*;
import com.trabalhopm.folha_pagamento.domain.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        String arquivo = "dados.txt";

        try (BufferedReader br = new BufferedReader(new FileReader(arquivo))) {
            String linha = br.readLine(); // lê cabeçalho e ignora
            while ((linha = br.readLine()) != null) {
                String[] partes = linha.split(";");

                // Dados do funcionário
                String nome = partes[0];
                String cpf = partes[1];
                String cargo = partes[2];
                LocalDate dataAdmissao = LocalDate.parse(partes[3]);
                byte dependentes = Byte.parseByte(partes[4]);
                byte insalubridade = Byte.parseByte(partes[5]);
                boolean periculosidade = Boolean.parseBoolean(partes[6]);

                // Dados financeiros
                BigDecimal salarioBruto = new BigDecimal(partes[7]);
                double horasDia = Double.parseDouble(partes[8]);
                byte diasSemana = Byte.parseByte(partes[9]);
                BigDecimal valeTransporteDia = new BigDecimal(partes[10]);
                BigDecimal valeAlimentacaoDia = new BigDecimal(partes[11]);

                // Monta objetos
                Financeiro financeiro = new Financeiro(
                        salarioBruto, horasDia, diasSemana,
                        valeTransporteDia, valeAlimentacaoDia
                );

                Funcionario funcionario = new Funcionario();
                funcionario.setNome(nome);
                funcionario.setCpf(cpf);
                funcionario.setCargo(cargo);
                funcionario.setDataAdmissao(dataAdmissao);
                funcionario.setNumeroDeDependentes(dependentes);
                funcionario.setNivelInsalubridade(insalubridade);
                funcionario.setTemPericulosidade(periculosidade);
                funcionario.setFinanceiro(financeiro);

                // Calcula folha
                CalculadoraSalario calc = new CalculadoraSalario();
                calc.adicionarDesconto(new INSS());
                calc.adicionarDesconto(new IRRF());
                calc.adicionarDesconto(new ValeTransporte());
                calc.adicionarDesconto(new ValeAlimentacao(financeiro.getCustoValeAlimentacaoMensal()));

                System.out.println("\n=== Folha de Pagamento de " + funcionario.getNome() + " ===");
                BigDecimal salarioLiquido = calc.calcularSalarioLiquido(financeiro.getSalarioBruto());
                System.out.println("Salário Líquido: R$ " + salarioLiquido);
                System.out.println("-----------------------------------");
            }
        } catch (IOException e) {
            System.out.println("Erro ao ler o arquivo: " + e.getMessage());
        }
    }
}
