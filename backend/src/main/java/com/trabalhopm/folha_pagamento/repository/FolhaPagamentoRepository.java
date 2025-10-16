package com.trabalhopm.folha_pagamento.repository;

import com.trabalhopm.folha_pagamento.domain.FolhaPagamento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FolhaPagamentoRepository extends JpaRepository<FolhaPagamento, Long> {
}
