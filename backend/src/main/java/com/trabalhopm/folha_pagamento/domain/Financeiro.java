package com.trabalhopm.folha_pagamento.domain;

import java.math.BigDecimal;

public class Financeiro {
    private BigDecimal salarioBruto;
    private double horasTrabalhadasDia;
    private byte diasTrabalhadosSemana;
    private BigDecimal valorDiarioValeTransporte;
    private BigDecimal valorDiarioValeAlimentacao;
    private BigDecimal horaExtraMensal = BigDecimal.ZERO;

    public Financeiro(BigDecimal salarioBruto, double horasTrabalhadasDia,
                      byte diasTrabalhadosSemana,
                      BigDecimal valorDiarioValeTransporte,
                      BigDecimal valorDiarioValeAlimentacao) {
        setSalarioBruto(salarioBruto);
        setHorasTrabalhadasDia(horasTrabalhadasDia);
        setDiasTrabalhadosSemana(diasTrabalhadosSemana);
        setValorDiarioValeTransporte(valorDiarioValeTransporte);
        setValorDiarioValeAlimentacao(valorDiarioValeAlimentacao);
    }

    public BigDecimal getSalarioBruto() { return salarioBruto; }
    public double getHorasTrabalhadasDia() { return horasTrabalhadasDia; }
    public byte getDiasTrabalhadosSemana() { return diasTrabalhadosSemana; }
    public BigDecimal getValorDiarioValeTransporte() { return valorDiarioValeTransporte; }
    public BigDecimal getValorDiarioValeAlimentacao() { return valorDiarioValeAlimentacao; }
    public BigDecimal getHoraExtraMensal() { return horaExtraMensal; }

    public void setSalarioBruto(BigDecimal salarioBruto) {
        if (salarioBruto == null || salarioBruto.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Salário bruto deve ser maior que zero.");
        }
        this.salarioBruto = salarioBruto;
    }

    public void setHorasTrabalhadasDia(double horasTrabalhadasDia) {
        if (horasTrabalhadasDia <= 0) {
            throw new IllegalArgumentException("Horas trabalhadas devem ser > 0.");
        }
        if (horasTrabalhadasDia > 8) {
            this.horaExtraMensal = BigDecimal.valueOf(horasTrabalhadasDia - 8);
            this.horasTrabalhadasDia = 8;
        } else {
            this.horasTrabalhadasDia = horasTrabalhadasDia;
        }
    }

    public void setDiasTrabalhadosSemana(byte diasTrabalhadosSemana) {
        if (diasTrabalhadosSemana < 0 || diasTrabalhadosSemana > 7) {
            throw new IllegalArgumentException("Dias trabalhados na semana devem estar entre 0 e 7.");
        }
        this.diasTrabalhadosSemana = diasTrabalhadosSemana;
    }

    public void setValorDiarioValeTransporte(BigDecimal valor) {
        if (valor.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Vale transporte não pode ser negativo.");
        }
        this.valorDiarioValeTransporte = valor;
    }

    public void setValorDiarioValeAlimentacao(BigDecimal valor) {
        if (valor.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Vale alimentação não pode ser negativo.");
        }
        this.valorDiarioValeAlimentacao = valor;
    }

    // Cálculo de salário-hora (aprox. 4 semanas no mês)
    public BigDecimal getSalarioHora() {
        int horasMes = diasTrabalhadosSemana * (int) horasTrabalhadasDia * 4;
        return salarioBruto.divide(BigDecimal.valueOf(horasMes), 2, BigDecimal.ROUND_HALF_UP);
    }

    // Custo mensal do VA
    public BigDecimal getCustoValeAlimentacaoMensal() {
        return valorDiarioValeAlimentacao.multiply(BigDecimal.valueOf(diasTrabalhadosSemana * 4));
    }

    // Custo mensal do VT
    public BigDecimal getCustoValeTransporteMensal() {
        return valorDiarioValeTransporte.multiply(BigDecimal.valueOf(diasTrabalhadosSemana * 4));
    }
}
