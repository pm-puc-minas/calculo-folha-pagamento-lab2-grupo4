package com.trabalhopm.folha_pagamento.utils.Feriado;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FeriadosNacionais {
    static final String WEB_SERVICE_URL = "https://brasilapi.com.br/api/feriados/v1/";
    static final int CODIGO_SUCESSO = 200;

    public static List<LocalDate> feriadosNoMes(LocalDate data) throws Exception {
        int ano = data.getYear();
        int mes = data.getMonthValue();
        List<LocalDate> feriados = new ArrayList<>();

        String urlParaChamada = WEB_SERVICE_URL + ano;

        try {
            URL url = new URL(urlParaChamada);
            HttpURLConnection conexao = (HttpURLConnection) url.openConnection();

            if (conexao.getResponseCode() != CODIGO_SUCESSO) {
                throw new RuntimeException("HTTP error code : " + conexao.getResponseCode());
            }

            String jsonCompleto;
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(conexao.getInputStream()))) {
                jsonCompleto = reader.lines().collect(Collectors.joining());
            }

            Gson gson = new GsonBuilder().registerTypeAdapter(
                    LocalDate.class,
                    (JsonDeserializer<LocalDate>) (json, type, context) ->
                            LocalDate.parse(json.getAsString(), DateTimeFormatter.ISO_LOCAL_DATE)
            ).create();

            Feriado[] feriadosArray = gson.fromJson(jsonCompleto, Feriado[].class);

            for (Feriado feriado : feriadosArray) {
                if (feriado.getDate().getMonthValue() == mes) {
                    feriados.add(feriado.getDate());
                }
            }

            conexao.disconnect();

            return feriados;
        }
        catch (Exception e) {
            throw new Exception("ERRO: " + e);
        }
    }
}