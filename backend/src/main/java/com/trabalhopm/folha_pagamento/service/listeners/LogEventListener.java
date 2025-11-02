package com.trabalhopm.folha_pagamento.service.listeners;

import com.trabalhopm.folha_pagamento.service.events.FuncionarioCadastradoEvent;
import com.trabalhopm.folha_pagamento.service.events.LoginSucessoEvent;
import com.trabalhopm.folha_pagamento.service.events.folha_events.FolhaDeletadaEvent;
import com.trabalhopm.folha_pagamento.service.events.folha_events.FolhaGeradaEvent;
import com.trabalhopm.folha_pagamento.service.events.folha_events.GetFolhaExistenteEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class LogEventListener {

    private static final Logger log = LoggerFactory.getLogger(LogEventListener.class);

    @EventListener
    public void handleLoginSucesso(LoginSucessoEvent event) {
        log.info(
                "EVENTO: Login bem-sucedido. ID: {}, Nome: {}, Login: {}",
                event.funcionario().getId(),
                event.funcionario().getNome(),
                event.funcionario().getLogin()
        );
    }

    @EventListener
    public void handleFuncionarioCadastrado(FuncionarioCadastradoEvent event) {
        log.info(
                "EVENTO: Novo funcionário cadastrado. ID: {}, Nome: {}, Login: {}",
                event.funcionario().getId(),
                event.funcionario().getNome(),
                event.funcionario().getLogin()
        );
    }

    @EventListener
    public void handleFolhaGerada(FolhaGeradaEvent event) {
        log.info(
                "LOG DE GERAÇÃO: Folha de pagamento gerada. Período: {}, Funcionário ID: {}",
                event.folhaPagamento().getPeriodo(),
                event.folhaPagamento().getFuncionario().getId()
        );
    }

    @EventListener
    public void handleGetFolhaExistente(GetFolhaExistenteEvent event) {
        log.info(
                "EVENTO: Folha de pagamento existente recuperada. Período: {}, Funcionário ID: {}",
                event.folhaPagamento().getPeriodo(),
                event.folhaPagamento().getFuncionario().getId()
        );
    }

    @EventListener
    public void handleFolhaDeletada(FolhaDeletadaEvent event) {
        log.info(
                "EVENTO: Folha de pagamento deletada. ID: {}, Período: {}, Funcionário ID: {}",
                event.folhaPagamento().getId(),
                event.folhaPagamento().getPeriodo(),
                event.folhaPagamento().getFuncionario().getId()
        );
    }
}
