package com.trabalhopm.folha_pagamento.service;

import com.trabalhopm.folha_pagamento.domain.FolhaPagamento;
import com.trabalhopm.folha_pagamento.domain.Funcionario;
import com.trabalhopm.folha_pagamento.repository.FolhaPagamentoRepository;

import com.trabalhopm.folha_pagamento.service.desconto.IDesconto;
import com.trabalhopm.folha_pagamento.service.encargoSocial.IEncargoSocial;
import com.trabalhopm.folha_pagamento.service.events.folha_events.FolhaDeletadaEvent;
import com.trabalhopm.folha_pagamento.service.events.folha_events.FolhaGeradaEvent;
import com.trabalhopm.folha_pagamento.service.events.folha_events.GetFolhaExistenteEvent;
import com.trabalhopm.folha_pagamento.service.provento.IProvento;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class FolhaPagamentoService {

    @Autowired
    private FolhaPagamentoRepository folhaPagamentoRepository;

    @Autowired
    private SalarioHoraService salarioHoraService;

    @Autowired
    private List<IProvento> proventos;

    @Autowired
    private List<IDesconto> descontos;

    @Autowired
    private List<IEncargoSocial> encargos;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Transactional
    public FolhaPagamento getFolha(Funcionario funcionario, YearMonth periodo) throws Exception {

        Optional<FolhaPagamento> folhaExistente = folhaPagamentoRepository.findByFuncionarioAndPeriodo(funcionario, periodo);

        if (folhaExistente.isPresent()) {
            eventPublisher.publishEvent(new GetFolhaExistenteEvent(folhaExistente.get()));
            return folhaExistente.get();
        }

        FolhaPagamento novaFolha = gerarFolha(funcionario, periodo);

        FolhaPagamento folhaSalva = folhaPagamentoRepository.save(novaFolha);

        eventPublisher.publishEvent(new FolhaGeradaEvent(folhaSalva));

        return folhaSalva;
    }

    public FolhaPagamento gerarFolha(Funcionario funcionario, YearMonth periodo) throws Exception {
        FolhaPagamento folhaPagamento = new FolhaPagamento(funcionario, periodo);

        folhaPagamento.setValorSalarioHora(salarioHoraService.calcularSalarioHora(funcionario));

        Map<String, BigDecimal> proventosCalculados = calcularProventos(funcionario, periodo);
        Map<String, BigDecimal> descontosCalculados = calcularDescontos(funcionario, periodo);
        Map<String, BigDecimal> encargosCalculados = calcularEncargoSocial(funcionario);

        BigDecimal totalProventos = proventosCalculados.getOrDefault("total", BigDecimal.ZERO);
        BigDecimal totalDescontos = descontosCalculados.getOrDefault("total", BigDecimal.ZERO);

        folhaPagamento.setTotalProventos(totalProventos);
        folhaPagamento.setTotalDescontos(totalDescontos);

        folhaPagamento.setValorINSS(descontosCalculados.getOrDefault("INSS", BigDecimal.ZERO));
        folhaPagamento.setValorIRRF(descontosCalculados.getOrDefault("IRRF", BigDecimal.ZERO));
        folhaPagamento.setDescontoValeTransporte(descontosCalculados.getOrDefault("DescontoValeTransporte", BigDecimal.ZERO));

        folhaPagamento.setSalarioFamilia(proventosCalculados.getOrDefault("SalarioFamilia", BigDecimal.ZERO));
        folhaPagamento.setAdicionalFerias(proventosCalculados.getOrDefault("Ferias", BigDecimal.ZERO));
        folhaPagamento.setValeAlimentacao(proventosCalculados.getOrDefault("ValeAlimentacao", BigDecimal.ZERO));
        folhaPagamento.setProventoValeTransporte(proventosCalculados.getOrDefault("ProventoValeTransporte", BigDecimal.ZERO));
        folhaPagamento.setInsalubridade(proventosCalculados.getOrDefault("Insalubridade", BigDecimal.ZERO));
        folhaPagamento.setPericulosidade(proventosCalculados.getOrDefault("Periculosidade", BigDecimal.ZERO));

        folhaPagamento.setValorFGTS(encargosCalculados.getOrDefault("FGTS", BigDecimal.ZERO));

        BigDecimal salarioLiquido = calcularSalarioLiquido(funcionario, totalDescontos, totalProventos);

        folhaPagamento.setSalarioLiquido(salarioLiquido);

        return folhaPagamento;
    }

    public HashMap<String, BigDecimal> calcularDescontos(Funcionario funcionario, YearMonth periodo) throws Exception {
        HashMap<String, BigDecimal> descontosCalculados = new HashMap<>();
        BigDecimal totalDescontos = BigDecimal.ZERO;

        for (IDesconto d : descontos) {
            BigDecimal valorDesconto = d.calcular(funcionario, periodo);
            descontosCalculados.put(d.getNome(), valorDesconto);

            totalDescontos = totalDescontos.add(valorDesconto);
        }

        descontosCalculados.put("total", totalDescontos);

        return descontosCalculados;
    }

    public HashMap<String, BigDecimal> calcularProventos(Funcionario funcionario, YearMonth periodo) throws Exception {
        HashMap<String, BigDecimal> proventosCalculados = new HashMap<>();
        BigDecimal totalProventos = BigDecimal.ZERO;

        for (IProvento p : proventos) {
            BigDecimal valorProvento = p.calcular(funcionario, periodo);
            proventosCalculados.put(p.getNome(), valorProvento);

            if (!p.getNome().equals("ValeAlimentacao") && !p.getNome().equals("ProventoValeTransporte")) {
                totalProventos = totalProventos.add(valorProvento);
            }
        }

        proventosCalculados.put("total", totalProventos);

        return proventosCalculados;
    }

    public HashMap<String, BigDecimal> calcularEncargoSocial(Funcionario funcionario) throws Exception {
        HashMap<String, BigDecimal> encargoSocialCalculados = new HashMap<>();

        for (IEncargoSocial e : encargos) {
            BigDecimal valorEncargo = e.calcular(funcionario);
            encargoSocialCalculados.put(e.getNome(), valorEncargo);
        }

        return encargoSocialCalculados;
    }

    public BigDecimal calcularSalarioLiquido(Funcionario funcionario, BigDecimal descontos, BigDecimal proventos) {
        BigDecimal salarioBruto = funcionario.getFinanceiro().getSalarioBruto();

        return salarioBruto.add(proventos).subtract(descontos);
    }

    public FolhaPagamento findByFuncionarioAndPeriodo(Funcionario funcionario, YearMonth periodo) throws Exception {
        Optional<FolhaPagamento> obj = folhaPagamentoRepository.findByFuncionarioAndPeriodo(funcionario, periodo);
        return obj.get();
    }

    public List<FolhaPagamento> findAllByFuncionario(Funcionario funcionario) {
        return folhaPagamentoRepository.findAllByFuncionario(funcionario);
    }

    public List<FolhaPagamento> findAllByPeriodo(YearMonth periodo) {
        return folhaPagamentoRepository.findAllByPeriodo(periodo);
    }

    public List<FolhaPagamento> findAll(){
        return folhaPagamentoRepository.findAll();
    }

    public FolhaPagamento findById(Long id){
        Optional<FolhaPagamento> obj = folhaPagamentoRepository.findById(id);
        return obj.orElseThrow(EntityNotFoundException::new);
    }

    @Transactional
    public void deleteByFuncionarioAndPeriodo(Funcionario funcionario, YearMonth periodo) {
        FolhaPagamento folha = folhaPagamentoRepository.findByFuncionarioAndPeriodo(funcionario, periodo)
                .orElseThrow(() -> new EntityNotFoundException("Folha de pagamento não encontrada"));

        eventPublisher.publishEvent(new FolhaDeletadaEvent(folha));

        folhaPagamentoRepository.deleteById(folha.getId());
    }
}
