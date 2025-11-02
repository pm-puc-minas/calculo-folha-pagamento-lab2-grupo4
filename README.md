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
