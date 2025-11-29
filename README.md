# Sistema de Gestão de Folha de Pagamento

Trabalho final da disciplina **Programação Modular** – PUC Minas  
Professor: Paulo Henrique D. S. Coelho

## 🚀 Sobre o projeto

Um sistema de folha de pagamento é uma ferramenta essencial para o departamento de Recursos Humanos de uma empresa.
Este projeto tem como objetivo automatizar o processo de cálculo dos salários dos funcionários, aplicando os descontos obrigatórios (como impostos e contribuições) e benefícios, para determinar o valor líquido a ser pago em um período.

O desenvolvimento é guiado por princípios de qualidade de software, como modularidade, Programação Orientada a Objetos (POO), padrões SOLID e uma cobertura de testes unitários.

## 🛠️ Construído com

- **Java 21**
- **Spring Boot**
- **JUnit**
- **Maven**
- **Banco de Dados:** PostgreSQL
- **Frontend:** HTML5, Tailwind CSS e JavaScript

## 💻 Execução do Projeto

### 1. Backend (API Spring Boot)

**Configuração do Banco de Dados:**

1.  Verifique se você tem um banco de dados PostgreSQL ativo.
2.  Crie um banco de dados chamado `folhadb` e configure as credenciais.
4.  O Spring Boot cuidará da criação das tabelas automaticamente na primeira execução.

**Executando a Aplicação:**

1.  Abra um terminal e navegue até a pasta `/backend` do projeto.
2.  Execute o seguinte comando Maven:
    ```sh
    mvn spring-boot:run
    ```
3.  O servidor da API estará em execução em `http://localhost:8080`.

### 2. Frontend (HTML/CSS/JS)

Em breve será adicionado o guia de execução

---

## 📡 Endpoints da API (Resumo)

Para a documentação completa e interativa, acesse: `http://localhost:8080/swagger-ui.html` (com o backend em execução).

| Verbo | Endpoint | Protegido? | Descrição |
| :--- | :--- | :--- | :--- |
| `POST` | `/auth/login` | ❌ Não | Realiza a autenticação e retorna um token JWT. |
| `POST` | `/funcionarios` | ❌ Não | Cadastra um novo funcionário. |
| `GET` | `/funcionarios` | ✅ Sim | Lista todos os funcionários. |
| `GET` | `/funcionarios/{id}` | ✅ Sim | Busca um funcionário por ID. |
| `GET` | `/funcionarios/{id}/financeiro` | ✅ Sim | Busca os dados financeiros de um funcionário. |
| `POST` | `/funcionarios/{id}/folhas/{periodo}` | ✅ Sim | Gera ou busca a folha de pagamento de um funcionário para um período (ex: `2025-10`). |
| `GET` | `/funcionarios/{id}/folhas` | ✅ Sim | Lista todas as folhas de pagamento de um funcionário. |
| `DELETE` | `/funcionarios/{id}/folhas/{periodo}` | ✅ Sim | Deleta a folha de pagamento de um período específico. |
| `GET` | `/folhas` | ✅ Sim | Lista todas as folhas de pagamento no sistema. |
| `GET` | `/folhas/{id}` | ✅ Sim | Busca uma folha de pagamento pelo seu ID. |

## 📄 Documentação Completa

Para uma análise detalhada da arquitetura, incluindo Diagramas de Classe, Modelos de Tela, Casos de Teste e Cartões CRC, veja a documentação completa do projeto:

* **[Acessar a Documentação de Arquitetura e Análise](./docs/README.md)**

🎯 Padrão 1 — Strategy
📌 Descrição

O padrão Strategy permite encapsular algoritmos diferentes sob uma mesma interface, tornando-os intercambiáveis.
No sistema de folha, cada desconto, provento ou encargo social tem regras próprias, mas todos precisam ser calculados de maneira uniforme.

📌 Como foi aplicado

Foram criadas interfaces que representam as estratégias:
IDesconto → Estratégias de desconto (INSS, IRRF, Vale Transporte, etc.)
IProvento → Estratégias de provento (Vale Alimentação, Insalubridade, Salário Família…)
IEncargoSocial → Estratégias de encargos (FGTS)
Cada cálculo foi isolado em uma classe concreta:
INSS.java, IRRF.java, DescontoValeTransporte.java
ValeAlimentacao.java, Insalubridade.java, Ferias.java
FGTS.java

📌 Benefícios obtidos

Reduziu acoplamento com a classe de serviço (FolhaPagamentoService).
Permitiu adicionar/desativar cálculos sem mexer na estrutura base.
Aderência direta ao princípio SOLID Open/Closed (OCP).
Código mais limpo, flexível e fácil de testar.

🎯 Padrão 2 — Factory Method
📌 Descrição

O Factory Method centraliza a criação de objetos, evitando que classes dependam de instâncias concretas e reduzindo o acoplamento.

📌 Problema antes da refatoração

Antes da Sprint 4, o Spring injetava automaticamente todos os descontos através de:

@Autowired
private List<IDesconto> descontos;


Isso dificultava:

controle explícito de quais descontos existiam
testes unitários isolados
manutenção das regras

📌 Como foi aplicado

Criamos a classe:
/service/desconto/DescontoFactory.java
Código:
@Component
public class DescontoFactory {

    public List<IDesconto> getDescontos() {
        List<IDesconto> lista = new ArrayList<>();

        lista.add(new INSS());
        lista.add(new IRRF());
        lista.add(new DescontoValeTransporte());

        return lista;
    }
}

E alteramos no FolhaPagamentoService:

❌ Antes
@Autowired
private List<IDesconto> descontos;

for (IDesconto d : descontos) {
    totalDescontos = totalDescontos.add(d.calcular(funcionario, periodo));
}

✅ Depois (com Factory Method)
@Autowired
private DescontoFactory descontoFactory;

for (IDesconto d : descontoFactory.getDescontos()) {
    BigDecimal valorDesconto = d.calcular(funcionario, periodo);
    totalDescontos = totalDescontos.add(valorDesconto);
}

📌 Benefícios

Centralização da lógica de criação das estratégias
Controle total sobre quais descontos estão ativos
Facilita adição de novos cálculos sem alterar o serviço
Código mais claro e organizado
Prepara o sistema para extensões futuras (ex.: Decorator)

📊 Diagrama UML — Strategy + Factory Method
Strategy (Descontos)
           +----------------------+
           |      IDesconto       | <<interface>>
           +----------------------+
           | + calcular()         |
           | + getNome()          |
           +----------+-----------+
                      |
     +----------------+-----------------------+
     |                |                       |
+----------+   +-------------+     +-------------------------+
|   INSS   |   |    IRRF     |     | DescontoValeTransporte |
+----------+   +-------------+     +-------------------------+

Factory Method
                    +-----------------------+
                    |   DescontoFactory     |
                    +-----------------------+
                    | + getDescontos()      |
                    +-----------+-----------+
                                |
                                v
                  retorna Lista<IDesconto>

🧩 Classes criadas / modificadas
✔ Criada

DescontoFactory.java
✔ Modificadas

FolhaPagamentoService.java (uso da Factory + substituição da lista injetada)

🧠 Conclusão
A aplicação dos padrões Strategy e Factory Method melhorou significativamente:
a organização da regra de negócio
a testabilidade
a extensibilidade do sistema
a clareza arquitetural
a aderência a padrões de mercado
O sistema agora está mais modular, limpo e preparado para evoluções futuras.