package com.trabalhopm.folha_pagamento.service.events;

import org.springframework.lang.NonNull;

public interface WebSocketMessage {
    @NonNull String getMessage();
}