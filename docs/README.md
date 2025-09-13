# Gestão de folha de pagamento

## Análise do estudo de caso

O domínio do sistema é o cálculo da folha de pagamento de uma empresa. A folha de pagamento é um documento mensal que detalha as remunerações, encargos e benefícios dos funcionários.

A folha de pagamento possui três propósitos principais:

* Obrigação legal:  É um documento exigido por lei que deve ser emitido mensalmente e arquivado pela empresa por um período mínimo de cinco anos.
* Ferramenta de Gestão: Permite à empresa controlar os custos com funcionários e acompanhar a evolução das remunerações e encargos sociais.
* Transparência para o Funcionário: Serve como um registro para que o colaborador possa verificar se o valor pago está correto e se seus direitos trabalhistas estão sendo respeitados.

O processo de cálculo parte do salário bruto, sobre o qual são aplicados os adicionais e benefícios, e são deduzidos os descontos. O objetivo final do sistema é automatizar essa rotina para apurar o salário líquido e exibir um relatório detalhado.

## Cartões CRC

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
