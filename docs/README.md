# Gestão de folha de pagamento

## Análise do estudo de caso

O domínio do sistema é o cálculo da folha de pagamento de uma empresa. A folha de pagamento é um documento mensal que detalha as remunerações, encargos e benefícios dos funcionários.

A folha de pagamento possui três propósitos principais:

* Obrigação legal:  É um documento exigido por lei que deve ser emitido mensalmente e arquivado pela empresa por um período mínimo de cinco anos.
* Ferramenta de Gestão: Permite à empresa controlar os custos com funcionários e acompanhar a evolução das remunerações e encargos sociais.
* Transparência para o Funcionário: Serve como um registro para que o colaborador possa verificar se o valor pago está correto e se seus direitos trabalhistas estão sendo respeitados.

O processo de cálculo parte do salário bruto, sobre o qual são aplicados os adicionais e benefícios, e são deduzidos os descontos. O objetivo final do sistema é automatizar essa rotina para apurar o salário líquido e exibir um relatório detalhado.

## Diagrama de Classes

![Diagrama UML](images/diagrama-uml.png)

## Modelos das telas do frontend

Tela de login

![](images/interface-login.png)

Tela inicial, indica as principais funcionalidades, mostra as folha de pagamentos recentes e os índices mensais.

![](images/interface-1.png)

Acesso aos funcionários cadastrados, sendo possível pesquisar por nome e ver sobre um funcionário específico.

![](images/interface-2.png)

Visão geral da folha de pagamento, com os benefícios, salário base e descontos.

![](images/interface-3.png)

Acesso aos gráficos e estatísticas do salário mensal de todos os funcionários.

![](images/interface-5.png)
![](images/interface-6.png)

## Casos de teste

| **ID** | **Classe / Funcionalidade**            | **Objetivo**                                           | **Entrada**                                          | **Resultado Esperado**                                 |
| ------ | -------------------------------------- | ------------------------------------------------------ | ---------------------------------------------------- | ------------------------------------------------------ |
| CT01   | **Funcionario**                        | Validar cadastro de funcionário com dados obrigatórios | Nome, CPF, cargo, data admissão, dependentes         | Funcionário criado corretamente                        |
| CT02   | **Financeiro**                         | Calcular salário bruto por hora/dia                    | Salário base, horas trabalhadas, dias úteis          | Salário bruto calculado                                |
| CT03   | **FolhaPagamento**                     | Processar folha com proventos e descontos              | Funcionário, período, lista de proventos e descontos | Folha gerada com salário líquido                       |
| CT04   | **Provento – ValeAlimentacao**         | Validar cálculo do benefício de vale alimentação       | Valor diário e dias trabalhados                      | Valor total de VA calculado                            |
| CT05   | **Provento – AdicionalPericulosidade** | Validar adicional de periculosidade                    | Salário base e percentual adicional                  | Adicional calculado corretamente                       |
| CT06   | **Provento – AdicionalInsalubridade**  | Validar adicional de insalubridade                     | Salário base e nível insalubridade                   | Valor adicional calculado                              |
| CT07   | **Provento – HoraExtra**               | Validar cálculo de hora extra                          | Nº de horas extras, valor hora                       | Valor de horas extras correto                          |
| CT08   | **Provento – Férias**                  | Garantir cálculo proporcional de férias                | Salário base, período aquisitivo                     | Valor de férias calculado                              |
| CT09   | **Provento – Salário Família**         | Validar cálculo do salário família                     | Nº de dependentes                                    | Valor proporcional calculado                           |
| CT10   | **Desconto – INSS**                    | Validar desconto por faixas salariais                  | Salário bruto                                        | Desconto conforme tabela oficial                       |
| CT11   | **Desconto – IRRF**                    | Validar desconto de IRRF                               | Base de cálculo, dependentes                         | Desconto aplicado corretamente                         |
| CT12   | **Desconto – Vale Transporte**         | Validar desconto de VT                                 | Salário bruto e % definido                           | Desconto aplicado corretamente                         |
| CT13   | **Desconto – Advertência**             | Garantir aplicação de desconto por advertência         | Registro de advertência                              | Valor abatido corretamente                             |
| CT14   | **Encargo Social – FGTS**              | Validar cálculo do FGTS                                | Salário bruto                                        | 8% do salário bruto registrado                         |
| CT15   | **Relatorio**                          | Gerar relatório de folha de pagamento                  | Folha processada                                     | Relatório com cabeçalho, proventos, descontos e resumo |

---

# Documentação - Cartões CRC

Os cartões CRC (Classe – Responsabilidade – Colaboração) são uma técnica utilizada no processo de modelagem orientada a objetos.  
Eles ajudam a representar de forma simples e visual as principais responsabilidades de uma classe e como ela se relaciona com outras classes dentro de um sistema.

---

---

## Cartões CRC

### Funcionário
**Responsabilidades:**
- Armazenar dados pessoais e contratuais (nome, CPF, cargo, salário bruto, dependentes, data de admissão).  
- Fornecer informações necessárias para cálculos da folha.  

**Colaborações:**
- Folha de Pagamento (fornece os dados para cálculos).  
- Cálculo Financeiro (usado indiretamente para cálculos).  

---

### Folha de Pagamento
**Responsabilidades:**
- Gerenciar o vínculo com o funcionário.  
- Guardar dados da folha (mês de referência, salário hora, salário líquido).  
- Gerar relatório consolidado da folha de pagamento.  
- Agregar proventos e descontos.  

**Colaborações:**
- Funcionário (para obter salário bruto, dependentes etc.).  
- Cálculo Financeiro (para realizar os cálculos).  
- Provento e Desconto (lista de itens que compõem a folha).  

---

### Cálculo Financeiro
**Responsabilidades:**
- Calcular salário líquido.  
- Calcular salário hora.  

**Colaborações:**
- Funcionário (usa seus dados para calcular valores).  
- Folha de Pagamento (retorna valores para serem exibidos/armazenados).  

---

### Provento (Interface)
**Responsabilidades:**
- Representar um item de ganho na folha (salário base, benefícios, adicionais).  
- Armazenar descrição e valor.  

**Colaborações:**
- Folha de Pagamento.  

**Exemplos de Proventos:**
- Adicional de Periculosidade  
- Adicional de Insalubridade  
- Vale Alimentação  

---

### Desconto (Interface)
**Responsabilidades:**
- Exibir detalhamento de proventos, descontos e valor líquido.  

**Colaborações:**
- Folha de Pagamento.  

**Exemplos de Descontos:**
- Desconto INSS  
- Desconto IRFF  
- Cálculo FGTS  
- Vale Alimentação  

---

### Vale Transporte (Interface Provento e Desconto)
**Responsabilidades:**
- Representar o benefício Vale Transporte, podendo atuar tanto como Provento quanto como Desconto.  

**Colaborações:**
- Folha de Pagamento.  

---

### Relatório
**Responsabilidades:**
- Saber os dados do funcionário, financeiro, proventos e descontos.  
- Exibir um relatório detalhado com os dados.  

**Colaborações:**
- Folha de Pagamento  
- Funcionário  
- Financeiro 

# ⚙️ Backend: Motor de Cálculo da Folha de Pagamento (Java/Spring Boot)

Este é o módulo Backend do sistema de Folha de Pagamento. Ele é o coração da aplicação, responsável por implementar todas as **regras de negócio**, realizar os **cálculos complexos** de salários, impostos e benefícios, e gerenciar a **persistência de dados**.

## 🚀 Arquitetura e Padrões de Design

O Backend é construído com base em uma arquitetura limpa e modular (N-Tier), utilizando o framework Spring Boot para garantir a escalabilidade e a manutenibilidade do código.

### 1. Estrutura de Camadas (N-Tier)

O código é rigorosamente dividido em pacotes que representam as camadas da aplicação, seguindo o padrão MVC/Service:

| Pacote | Camada | Responsabilidade |
| :--- | :--- | :--- |
| `controller` | Apresentação/API | Recebe requisições HTTP (REST), valida a entrada e delega para o `Service`. |
| `service` | Regras de Negócio | Contém a lógica de cálculo da folha de pagamento, aplicação de regras de IRRF, INSS, e gestão de entidades. |
| `repository` | Acesso a Dados (DAO) | Interage diretamente com o Banco de Dados (via Spring Data JPA) para operações CRUD. |
| `model / entity` | Domínio | Classes que representam os objetos de negócio e o mapeamento para as tabelas do BD. |
| `dto` | Transferência de Dados | Objetos usados para comunicação entre o `Controller` e o `Service`, garantindo a separação de responsabilidades. |

### 2. Princípios de Orientação a Objetos

O código segue os princípios **SOLID** para garantir um design robusto:

* **Single Responsibility Principle (SRP):** Cada classe e método possui uma única responsabilidade.
* **Dependency Inversion Principle (DIP):** Uso de interfaces no pacote `Service` (ex: `FolhaPagamentoService`) para desacoplar as implementações.
* **Programação Orientada a Objetos (POO):** Uso extensivo de herança, encapsulamento e polimorfismo, especialmente no tratamento de diferentes tipos de funcionários ou regras de cálculo.

## 📐 Modelo de Classes (Baseado em Diagramas UML)

Com base nos requisitos de Folha de Pagamento, o modelo de domínio (classes no pacote `model/entity`) inclui as seguintes entidades chave, mapeadas para o banco de dados:

| Entidade | Atributos Chave | Relacionamentos Principais |
| :--- | :--- | :--- |
| **Funcionario** | `id`, `nome`, `cpf`, `cargo`, `salarioBase` | **1:N** com `RegistroPonto`, **1:N** com `FolhaPagamento` |
| **FolhaPagamento** | `id`, `mes`, `ano`, `salarioBruto`, `totalDescontos`, `salarioLiquido` | **N:1** com `Funcionario`, **1:N** com `ItemDesconto` |
| **ItemDesconto** | `id`, `tipo` (`INSS`, `IRRF`, `ValeTransporte`), `valor` | **N:1** com `FolhaPagamento` |
| **RegistroPonto** | `id`, `data`, `entrada`, `saida`, `horasExtras` | **N:1** com `Funcionario` |
| **TabelaINSS / TabelaIRRF** | `faixaInicial`, `faixaFinal`, `aliquota`, `deducao` | Estruturas estáticas usadas pelo `Service` para o cálculo. |

## 🔗 Interfaces e Comunicação (Endpoints REST)

O Backend expõe uma API RESTful para comunicação com o Frontend e outros sistemas, utilizando o padrão **JSON** para troca de dados.

### 1. Módulo de Funcionários (`/api/v1/funcionarios`)

| Método | Endpoint | Descrição |
| :--- | :--- | :--- |
| `GET` | `/` | Lista todos os funcionários registrados. |
| `GET` | `/{id}` | Busca os dados de um funcionário específico. |
| `POST` | `/` | Cadastra um novo funcionário no sistema. |
| `PUT` | `/{id}` | Atualiza os dados de um funcionário existente. |

### 2. Módulo de Cálculo da Folha (`/api/v1/folha`)

| Método | Endpoint | Descrição |
| :--- | :--- | :--- |
| `POST` | `/processar/{mes}/{ano}` | **Gera a folha de pagamento** para todos os funcionários em um determinado mês/ano. |
| `GET` | `/funcionario/{idFuncionario}/{mes}/{ano}` | Busca o detalhe da folha de pagamento de um funcionário para um período. |
| `GET` | `/relatorio/{mes}/{ano}` | Gera um relatório consolidado da folha de pagamento do período. |

## 🛠️ Configuração e Dependências

### Dependências Principais (via `pom.xml` - Maven)

* `spring-boot-starter-web`: Suporte a RESTful APIs.
* `spring-boot-starter-data-jpa`: Persistência de dados e mapeamento ORM (Hibernate).
* `(driver-do-banco)`: Ex: `postgresql` ou `mysql-connector-java`.
* `spring-boot-starter-test`: Módulos de teste (JUnit 5, Mockito).

### Configuração do Banco de Dados

As configurações de conexão (URL, usuário e senha) são definidas no arquivo `application.properties` ou `application.yml`.

**Exemplo de Configuração (PostgreSQL):**

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/folha_db
spring.datasource.username=user
spring.datasource.password=password
spring.jpa.hibernate.ddl-auto=update # Ou create-drop para desenvolvimento

---
