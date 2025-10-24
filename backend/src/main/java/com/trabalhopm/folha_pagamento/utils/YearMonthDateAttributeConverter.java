package com.trabalhopm.folha_pagamento.utils;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.time.LocalDate;
import java.time.YearMonth;

@Converter(autoApply = true)
public class YearMonthDateAttributeConverter implements AttributeConverter<YearMonth, LocalDate> {
    /**
     * Converte o YearMonth (ex: 2025-10) da entidade para
     * um LocalDate (ex: 2025-10-01) para ser salvo no banco (coluna DATE).
     * Usamos o primeiro dia do mês como representação.
     */
    @Override
    public LocalDate convertToDatabaseColumn(YearMonth attribute) {
        if (attribute != null) {
            return attribute.atDay(1);
        }
        return null;
    }

    /**
     * Converte o LocalDate (ex: 2025-10-01) vindo do banco (coluna DATE)
     * para um YearMonth (ex: 2025-10) na entidade.
     */
    @Override
    public YearMonth convertToEntityAttribute(LocalDate dbData) {
        if (dbData != null) {
            return YearMonth.from(dbData);
        }
        return null;
    }
}
