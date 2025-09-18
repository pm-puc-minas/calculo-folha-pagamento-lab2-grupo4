package com.trabalhopm.folha_pagamento;
import java.math.BigDecimal;

public class Financeiro {
    private BigDecimal salarioBruto;
    private double horasTrabalhadasDia;
    private byte diasTrabalhadosSemana;
    private BigDecimal valorDiarioValeTransporte;
    private BigDecimal valorDiarioValeAlimentacao;
    private double horaExtraMensal;

    public Financeiro(BigDecimal salarioBruto, double horasTrabalhadasDia, byte diasTrabalhadosSemana, BigDecimal valorDiarioValeTransporte, BigDecimal valorDiarioValeAlimentacao) {
        this.salarioBruto = salarioBruto;
        this.horasTrabalhadasDia = horasTrabalhadasDia;
        this.diasTrabalhadosSemana = diasTrabalhadosSemana;
        this.valorDiarioValeTransporte = valorDiarioValeTransporte;
        this.valorDiarioValeAlimentacao = valorDiarioValeAlimentacao;
    }

    public double getHoraExtraMensal() {
        return horaExtraMensal;
    }

    public byte getDiasTrabalhadosSemana() {
        return diasTrabalhadosSemana;
    }

    public double getHorasTrabalhadasDia() {
        return horasTrabalhadasDia;
    }
    
    public BigDecimal getSalarioBruto() {
        return salarioBruto;
    }
    
    public BigDecimal getValorDiarioValeAlimentacao() {
        return valorDiarioValeAlimentacao;
    }
    
    public BigDecimal getValorDiarioValeTransporte() {
        return valorDiarioValeTransporte;
    }
    
    public void setDiasTrabalhadosSemana(byte diasTrabalhadosSemana) {
        if(diasTrabalhadosSemana < 0 || diasTrabalhadosSemana > 7) {
            throw new IllegalArgumentException("Dias trabalhados na semana devem estar entre 0 e 7.");
        }
        this.diasTrabalhadosSemana = diasTrabalhadosSemana;
    }
    
    public void setHorasTrabalhadasDia(double horasTrabalhadasDia) {
        if(horasTrabalhadasDia <= 0 && horasTrabalhadasDia >= 8) {
            this.horasTrabalhadasDia = horasTrabalhadasDia;
        }
        else if (horasTrabalhadasDia > 8) {
            this.horaExtraMensal = horasTrabalhadasDia - 8;
            this.horasTrabalhadasDia = 8;
        }
        else 
            throw new IllegalArgumentException("Horas trabalhadas no dia devem ser maiores que 0 e no máximo 8.");

        this.horasTrabalhadasDia = horasTrabalhadasDia;
    }
    
    public void setSalarioBruto(BigDecimal salarioBruto) {
        if (salarioBruto.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Salário bruto deve ser maior que zero.");
            
        }
        this.salarioBruto = salarioBruto;
    }
    
    public void setValorDiarioValeAlimentacao(BigDecimal valorDiarioValeAlimentacao) {
        if (valorDiarioValeAlimentacao.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Valor diário do vale alimentação não pode ser negativo.");
            
        }
        this.valorDiarioValeAlimentacao = valorDiarioValeAlimentacao;
    }
    
    public void setValorDiarioValeTransporte(BigDecimal valorDiarioValeTransporte) {
        if (valorDiarioValeTransporte.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Valor diário do vale transporte não pode ser negativo.");
            
        }
        this.valorDiarioValeTransporte = valorDiarioValeTransporte;
    }
}
