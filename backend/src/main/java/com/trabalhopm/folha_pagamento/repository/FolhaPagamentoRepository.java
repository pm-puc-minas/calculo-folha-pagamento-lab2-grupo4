package com.trabalhopm.folha_pagamento.repository;

import com.trabalhopm.folha_pagamento.domain.FolhaPagamento;
import com.trabalhopm.folha_pagamento.domain.Funcionario;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

public interface FolhaPagamentoRepository extends JpaRepository<FolhaPagamento, Long> {
    Optional<FolhaPagamento> findByFuncionarioAndPeriodo(Funcionario funcionario, YearMonth periodo);
    List<FolhaPagamento> findAllByFuncionario(Funcionario funcionario);
    List<FolhaPagamento> findAllByPeriodo(YearMonth periodo);
}
