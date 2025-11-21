package com.trabalhopm.folha_pagamento.domain;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.trabalhopm.folha_pagamento.domain.enums.NivelInsalubridade;
import com.trabalhopm.folha_pagamento.utils.ValidaCPF;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "funcionarios")
public class Funcionario extends Usuario implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotBlank(message = "O nome não pode estar em branco")
    private String nome;

    @NotBlank(message = "O CPF não pode estar em branco")
    private String cpf;

    @NotBlank
    private String telefone;

    @NotBlank(message = "O cargo não pode estar em branco")
    private String cargo;

    @Column(name = "data_nascimento")
    @Past(message = "Digite uma data de nascimento válida")
    private LocalDate dataNascimento;

    @Column(name = "data_admissao")
    @PastOrPresent(message = "Digite uma data de admissão válida")
    private LocalDate dataAdmissao;

    @Column(name = "numero_de_dependentes")
    @PositiveOrZero(message = "O número de dependentes não pode ser negativo")
    private byte numeroDeDependentes;

    @Column(name = "nivel_insalubridade")
    @Enumerated(EnumType.STRING)
    private NivelInsalubridade nivelInsalubridade;

    @Column(name = "tem_periculosidade")
    private boolean temPericulosidade;

    @Column(name = "ta_de_ferias")
    private boolean deFerias;

    @OneToOne(mappedBy = "funcionario", cascade = CascadeType.ALL)
    @JsonManagedReference
    private Financeiro financeiro;

    public void setCPF(String cpf) {
        if(!ValidaCPF.isCPF(cpf)) {
            throw new IllegalArgumentException("CPF Invalido");
        }
        this.cpf = cpf;
    }
}