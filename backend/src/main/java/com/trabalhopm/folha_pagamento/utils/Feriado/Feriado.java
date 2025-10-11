package com.trabalhopm.folha_pagamento.utils.Feriado;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class Feriado {
    private LocalDate date;
    private String name;
    private String type;
}