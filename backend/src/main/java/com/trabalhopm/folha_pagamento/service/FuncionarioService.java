package com.trabalhopm.folha_pagamento.service;

import com.trabalhopm.folha_pagamento.domain.Financeiro;
import com.trabalhopm.folha_pagamento.domain.Funcionario;
import com.trabalhopm.folha_pagamento.dto.FuncionarioDTO;
import com.trabalhopm.folha_pagamento.repository.FuncionarioRepository;
import com.trabalhopm.folha_pagamento.service.events.FuncionarioCadastradoEvent;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FuncionarioService {

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @Autowired
    private ApplicationEventPublisher eventPublisher;


    public List<Funcionario> findAll(){
        return funcionarioRepository.findAll();
    }

    public Funcionario findById(Long id){
        Optional<Funcionario> obj = funcionarioRepository.findById(id);
        return obj.orElseThrow(EntityNotFoundException::new);
    }

    @Transactional
    public Funcionario create(FuncionarioDTO funcionarioDTO){
        if (funcionarioRepository.findByLogin(funcionarioDTO.getLogin()) != null) {
            throw new IllegalArgumentException("Este login já está em uso.");
        }

        String senhaEncriptada = new BCryptPasswordEncoder().encode(funcionarioDTO.getSenha());

        Funcionario funcionario = new Funcionario();

        funcionario.setLogin(funcionarioDTO.getLogin());
        funcionario.setSenha(senhaEncriptada);
        funcionario.setTipo(funcionarioDTO.getTipoUsuario());

        funcionario.setNome(funcionarioDTO.getNome());
        funcionario.setCPF(funcionarioDTO.getCpf());
        funcionario.setCargo(funcionarioDTO.getCargo());
        funcionario.setDataAdmissao(funcionarioDTO.getDataAdmissao());
        funcionario.setNumeroDeDependentes(funcionarioDTO.getNumeroDeDependentes());
        funcionario.setNivelInsalubridade(funcionarioDTO.getNivelInsalubridade());
        funcionario.setTemPericulosidade(funcionarioDTO.isTemPericulosidade());
        funcionario.setDeFerias(funcionarioDTO.isDeFerias());

        Financeiro financeiro = new Financeiro();
        financeiro.setSalarioBruto(funcionarioDTO.getFinanceiro().getSalarioBruto());
        financeiro.setHorasTrabalhadasDia(funcionarioDTO.getFinanceiro().getHorasTrabalhadasDia());
        financeiro.setDiasTrabalhadosSemana(funcionarioDTO.getFinanceiro().getDiasTrabalhadosSemana());
        financeiro.setValorDiarioValeTransporte(funcionarioDTO.getFinanceiro().getValorDiarioValeTransporte());

        financeiro.setFuncionario(funcionario);
        funcionario.setFinanceiro(financeiro);

        Funcionario funcionarioSalvo = funcionarioRepository.save(funcionario);

        eventPublisher.publishEvent(new FuncionarioCadastradoEvent(funcionarioSalvo));

        return funcionarioSalvo;
    }
}
