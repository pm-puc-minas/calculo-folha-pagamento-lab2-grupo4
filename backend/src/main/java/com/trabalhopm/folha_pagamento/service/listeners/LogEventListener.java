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
import org.springframework.lang.NonNull;
import com.trabalhopm.folha_pagamento.service.WebSocketService;
import com.trabalhopm.folha_pagamento.service.events.WebSocketMessage;

@Component
public class LogEventListener {

    private static final Logger log = LoggerFactory.getLogger(LogEventListener.class);
    private final WebSocketService webSocketService;

    public LogEventListener(WebSocketService webSocketService) {
        this.webSocketService = webSocketService;
    }

    @EventListener
    public void handleLoginSucesso(@NonNull LoginSucessoEvent event) {
        String funcionarioNome = event.funcionario().getNome();
        String funcionarioLogin = event.funcionario().getLogin();
        WebSocketMessage wsMessage = () -> "Login bem-sucedido. Nome: " + funcionarioNome + ", Login: " + funcionarioLogin;
        
        log.info(
                "EVENTO: Login bem-sucedido. ID: {}, Nome: {}, Login: {}",
                event.funcionario().getId(),
                funcionarioNome,
                funcionarioLogin
        );
        webSocketService.sendLogMessage(wsMessage.getMessage());
    }

    @EventListener
    public void handleFuncionarioCadastrado(@NonNull FuncionarioCadastradoEvent event) {
        String funcionarioNome = event.funcionario().getNome();
        String funcionarioLogin = event.funcionario().getLogin();
        WebSocketMessage wsMessage = () -> "Novo funcionário cadastrado. Nome: " + funcionarioNome + ", Login: " + funcionarioLogin;
        
        log.info(
                "EVENTO: Novo funcionário cadastrado. ID: {}, Nome: {}, Login: {}",
                event.funcionario().getId(),
                funcionarioNome,
                funcionarioLogin
        );
        webSocketService.sendLogMessage(wsMessage.getMessage());
    }

    @EventListener
    public void handleFolhaGerada(@NonNull FolhaGeradaEvent event) {
        String funcionarioNome = event.folhaPagamento().getFuncionario().getNome();
        String mes = event.folhaPagamento().getPeriodo().getMonth().toString();
        String ano = String.valueOf(event.folhaPagamento().getPeriodo().getYear());
        WebSocketMessage wsMessage = () -> "Folha de pagamento gerada para " + funcionarioNome + " - Período: " + mes + "/" + ano;
        
        log.info(
                "LOG DE GERAÇÃO: Folha de pagamento gerada. Período: {}, Funcionário ID: {}",
                event.folhaPagamento().getPeriodo(),
                event.folhaPagamento().getFuncionario().getId()
        );
        webSocketService.sendLogMessage(wsMessage.getMessage());
    }

    @EventListener
    public void handleGetFolhaExistente(@NonNull GetFolhaExistenteEvent event) {
        String funcionarioNome = event.folhaPagamento().getFuncionario().getNome();
        String mes = event.folhaPagamento().getPeriodo().getMonth().toString();
        String ano = String.valueOf(event.folhaPagamento().getPeriodo().getYear());
        WebSocketMessage wsMessage = () -> "Folha de pagamento consultada para " + funcionarioNome + " - Período: " + mes + "/" + ano;
        
        log.info(
                "EVENTO: Folha de pagamento existente recuperada. Período: {}, Funcionário ID: {}",
                event.folhaPagamento().getPeriodo(),
                event.folhaPagamento().getFuncionario().getId()
        );
        webSocketService.sendLogMessage(wsMessage.getMessage());
    }

    @EventListener
    public void handleFolhaDeletada(@NonNull FolhaDeletadaEvent event) {
        String funcionarioNome = event.folhaPagamento().getFuncionario().getNome();
        String mes = event.folhaPagamento().getPeriodo().getMonth().toString();
        String ano = String.valueOf(event.folhaPagamento().getPeriodo().getYear());
        WebSocketMessage wsMessage = () -> "Folha de pagamento excluída para " + funcionarioNome + " - Período: " + mes + "/" + ano;
        
        log.info(
                "EVENTO: Folha de pagamento deletada. ID: {}, Período: {}, Funcionário ID: {}",
                event.folhaPagamento().getId(),
                event.folhaPagamento().getPeriodo(),
                event.folhaPagamento().getFuncionario().getId()
        );
        webSocketService.sendLogMessage(wsMessage.getMessage());
    }
}
