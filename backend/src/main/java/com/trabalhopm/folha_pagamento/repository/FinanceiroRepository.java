package com.trabalhopm.folha_pagamento.repository;

import com.trabalhopm.folha_pagamento.domain.Financeiro;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FinanceiroRepository extends JpaRepository<Financeiro, Long> {
}
