package com.trabalhopm.folha_pagamento.repository;

import com.trabalhopm.folha_pagamento.domain.Funcionario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FuncionarioRepository extends JpaRepository<Funcionario, Long> {
}
