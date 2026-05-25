# simulador-painel-investimentos-api

> **🚧 Work in Progress (WIP)**  
> Este projeto está em desenvolvimento ativo. Funcionalidades, contratos de API, regras de negócio, cobertura de testes e documentação ainda podem mudar.

API REST para simulação de investimentos, gestão de clientes, produtos financeiros e perfil de risco, construída com **Java 21** e **Quarkus**.

---

## Sumário

- [Visão geral](#visão-geral)
- [Tecnologias empregadas](#tecnologias-empregadas)
- [Arquitetura e estrutura do projeto](#arquitetura-e-estrutura-do-projeto)
- [Banco de dados e migrations](#banco-de-dados-e-migrations)
- [Documentação da API](#documentação-da-api)
- [Endpoints disponíveis](#endpoints-disponíveis)
    - [Clientes](#clientes)
    - [Perfil de risco](#perfil-de-risco)
    - [Produtos](#produtos)
    - [Simulações](#simulações)
- [Tratamento de erros](#tratamento-de-erros)
- [Testes](#testes) :hourglass_flowing_sand:
- [Execução local](#execução-local)
- [Variáveis e configurações importantes](#variáveis-e-configurações-importantes) :hourglass_flowing_sand:


---

## Visão geral

- cadastro e gestão de clientes 
- simulação de investimentos
- recomendação de investimentos baseada em perfil de risco
- perfil de risco dinâmico com base em comportamento de investimentos e/ou simulações
- telemetria de dados

---

## Tecnologias empregadas

Principais tecnologias identificadas em `pom.xml`:

- **Java 21**
- **Quarkus 3.34.3**
- **Docker**
- **Quarkus REST**
- **Jackson**
- **Hibernate ORM**
- **Panache**
- **Hibernate Validator**
- **Microsoft SQL Server**
- **Flyway**
- **SmallRye OpenAPI / Swagger UI**
- **OIDC / Keycloak**
- **MapStruct**
- **Lombok**
- **JUnit 5**
- **Mockito**
- **JaCoCo**

---

## Arquitetura e estrutura do projeto

Estrutura principal observada:

```text
src/main/java/master/gard
├── config
├── dto
├── exception
├── mapper
├── model
├── repository
├── resource
├── service
└── util

src/main/resources
├── application.properties
└── db/migration
```

---

## Endpoints disponíveis

### Clientes

**Base path:** `/api/v1/clientes`  
**Referência:** `src/main/java/master/gard/resource/cliente/ClienteResourceI.java`

| Método | Endpoint | Acesso | Descrição |
|:--|:--|:--|:--|
| `GET` | `/api/v1/clientes` | `admin` | Lista clientes com filtros, paginação e ordenação. |
| `GET` | `/api/v1/clientes/{id}` | `admin` | Retorna os dados de um cliente específico por ID. |
| `POST` | `/api/v1/clientes` | `admin`, `user` | Cadastra um novo cliente. |
| `PUT` | `/api/v1/clientes/{id}` | `admin` | Atualiza os dados de um cliente existente. |
| `GET` | `/api/v1/clientes/me` | `admin`, `user` | Retorna os dados do cliente autenticado. |
| `PUT` | `/api/v1/clientes/me` | `admin`, `user` | Atualiza os dados do cliente autenticado. |

**Recursos observados**
- suporte a filtros via `@BeanParam`
- validação de entrada com `@Valid` e `@NotNull`
- documentação OpenAPI por operação
- controle de acesso por perfil

---

### Perfil de risco

**Base path:** `/api/v1/perfil-risco`  
**Referência:** `src/main/java/master/gard/resource/cliente/PerfilRiscoResourceI.java`

| Método | Endpoint | Acesso | Descrição |
|:--|:--|:--|:--|
| `GET` | `/api/v1/perfil-risco/{clienteId}` | `admin` | Consulta o perfil de risco de um cliente por ID. |
| `GET` | `/api/v1/perfil-risco/me` | `admin`, `user` | Retorna o perfil de risco do cliente autenticado. |

**Recursos observados**
- consulta administrativa por identificador de cliente
- consulta do próprio usuário autenticado
- respostas documentadas no OpenAPI

---

### Produtos

**Base path:** `/api/v1/produtos`  
**Referência:** `src/main/java/master/gard/resource/produto/ProdutoResourceI.java`

| Método | Endpoint | Acesso | Descrição |
|:--|:--|:--|:--|
| `GET` | `/api/v1/produtos` | `admin` | Lista produtos financeiros com filtros, paginação e ordenação. |
| `GET` | `/api/v1/produtos/{id}` | `admin` | Retorna um produto financeiro específico por ID. |
| `POST` | `/api/v1/produtos` | `admin` | Cadastra um novo produto financeiro. |
| `PUT` | `/api/v1/produtos/{id}` | `admin` | Atualiza um produto financeiro existente. |

**Recursos observados**
- listagem com filtros
- validação de payload
- documentação OpenAPI por endpoint

> **Em andamento:** `DELETE` ainda não foi implementado, pois depende de validações envolvendo associações com simulações ou investimentos.

---

### Simulações

**Base path:** `/api/v1/simulacoes`  
**Referência:** `src/main/java/master/gard/resource/simulacao/SimulacaoResourceI.java`

| Método | Endpoint | Acesso | Descrição |
|:--|:--|:--|:--|
| `GET` | `/api/v1/simulacoes` | `admin`, `user` | Lista simulações com filtros. |
| `POST` | `/api/v1/simulacoes/simular-investimento` | `admin`, `user` | Executa uma simulação de investimento. |

**Recursos observados**
- suporte a filtros via `@BeanParam`
- validação de entrada com `@Valid` e `@NotNull`
- integração com documentação OpenAPI

---

### Resumo de acesso por perfil

| Perfil | Permissões  |
|:--|:--|
| `admin` | Acesso administrativo a clientes, produtos, perfil de risco e simulações. |
| `user` | Acesso ao próprio cadastro, próprio perfil de risco e simulações. |

---

## Banco de dados e migrations

A aplicação está configurada para utilizar **Microsoft SQL Server** (Docker) como banco de dados principal, com controle de versionamento do schema via **Flyway**.

---

### Resumo da configuração

| Item | Valor identificado |
|:--|:--|
| Banco de dados | `Microsoft SQL Server` |
| Driver | `quarkus-jdbc-mssql` |
| ORM | `Hibernate ORM` + `Panache` |
| Estratégia de schema | `none` |
| Migrations | `Flyway` |
| Execução das migrations | automática no startup |
| Local das migrations | `db/migration` |


---

### Migrations

| Versão | Arquivo | Objetivo |
|:--|:--|:--|
| `V1` | `V1__criar_tabelas.sql` | Criação da estrutura principal do banco |
| `V2` | `V2__script_clientes.sql` | Inserção de dados iniciais de clientes |
| `V3` | `V3__script_produtos.sql` | Inserção de dados iniciais de produtos |

---

## Documentação da API

A API possui documentação baseada em **OpenAPI**, com interface interativa disponível por meio do **Swagger UI**.

---

### Resumo da documentação

| Item | Valor identificado |
|:--|:--|
| Padrão de documentação | `OpenAPI` |
| Implementação | `SmallRye OpenAPI` |
| Interface visual | `Swagger UI` |
| Disponibilidade da UI | habilitada |
| Caminho configurado | `/swagger-ui` |

---

### Configuração encontrada

### Configuração central do OpenAPI

| Elemento | Valor observado |
|:--|:--|
| Título | `Painel de Investimentos API` |
| Versão | `1.0` |
| Server local | `http://localhost:8080` |
| Contato | `Giovanni Duarte` |
| E-mail de contato | `garduarte@teste.com` |

---

### Componentes reutilizáveis de resposta

#### Respostas identificadas

| Nome | HTTP | Descrição |
|:--|:--|:--|
| `BadRequest` | `400` | Requisição inválida |
| `NotFoundCliente` | `404` | Cliente não encontrado |
| `NotFoundProduto` | `404` | Produto não encontrado |
| `ConflictClienteDuplicado` | `409` | Conflito por cliente duplicado |
| `ConflictProdutoDuplicado` | `409` | Conflito por produto duplicado |


---

## Tratamento de erros

A API adota um padrão estruturado para respostas de erro com base no DTO `ProblemDetails`, permitindo mensagens mais consistentes, legíveis e documentadas.

### Estrutura padrão de erro

O payload de erro identificado no projeto é representado por:

- `src/main/java/master/gard/dto/exception/ProblemDetails.java`

#### Campos disponíveis

| Campo | Tipo | Descrição |
|:--|:--|:--|
| `type` | `String` | URI que identifica o tipo do problema |
| `title` | `String` | Título resumido e legível do erro |
| `status` | `int` | Código HTTP associado ao problema |
| `detail` | `String` | Descrição detalhada do erro |
| `instance` | `String` | URI da requisição que originou o problema |
| `violations` | `Map<String, List<String>>` | Violações de validação agrupadas por campo |

### Exemplo de resposta

```json
{
  "title": "Requisição inválida",
  "status": 400,
  "detail": "Um ou mais campos estão inválidos",
  "instance": "http://localhost:8080/api/v1/recurso",
  "violations": {
    "email": ["O campo 'email' deve ser um endereço válido."],
    "nome": ["O campo 'nome' é obrigatório."]
  }
}