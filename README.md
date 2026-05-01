# Project Task Management API

API REST para gestão de projetos e tarefas com autenticação JWT.

## Tecnologias

- Java 21
- Spring Boot
- Spring Security + JWT
- MySQL
- Maven

## Como executar localmente

1. Clone o repositório
2. Crie um ficheiro `.env` na raiz do projeto com base no `.env.example`:
```env
DB_PORT=use_a_porta_da_sua_base_de_dados
DB_NAME=use_o_nome_da_sua_base_de_dados
DB_USERNAME=root
DB_PASSWORD=a_tua_senha
JWT_SECRET=o_teu_secret
```
3. Execute:
```bash
./mvnw spring-boot:run
```

## Exemplos de uso

### Criar conta
`POST /auth/register`
```json
{
  "name": "João Silva",
  "login": "joao",
  "password": "123456"
}
```

### Login
`POST /auth/login`
```json
{
  "login": "joao",
  "password": "123456"
}
```
> O token retornado deve ser enviado no header `Authorization: Bearer {token}` em todos os outros endpoints.

---

### Criar projeto
`POST /projects`
```json
{
  "name": "Meu Projeto",
  "description": "Descrição do projeto"
}
```

### Adicionar membro ao projeto
`POST /projects/{id}/members`
```json
{
  "login": "maria"
}
```

---

### Criar tarefa
`POST /projects/{id}/tasks`
```json
{
  "title": "Implementar login",
  "description": "Criar endpoint de autenticação",
  "priority": "HIGH",
  "dueDate": "2025-12-31T23:59:59",
  "assignedToLogin": "maria"
}
```
> `priority` pode ser: `LOW`, `MEDIUM`, `HIGH`

### Atualizar tarefa
`PUT /projects/{id}/tasks/{taskId}`
```json
{
  "title": "Novo título",
  "status": "IN_PROGRESS",
  "priority": "MEDIUM",
  "assignedToLogin": "joao"
}
```
> `status` pode ser: `PENDING`, `IN_PROGRESS`, `COMPLETED`

---

## Endpoints

### Auth
| Método | Rota | Descrição |
|---|---|---|
| POST | /auth/register | Registar utilizador |
| POST | /auth/login | Login |

### Projetos
| Método | Rota | Descrição |
|---|---|---|
| POST | /projects | Criar projeto |
| GET | /projects | Listar projetos |
| GET | /projects?name= | Buscar projeto por nome |
| GET | /projects/{id} | Buscar projeto por id |
| PUT | /projects/{id} | Atualizar projeto |
| DELETE | /projects/{id} | Deletar projeto |
| POST | /projects/{id}/members | Adicionar membro |
| GET | /projects/{id}/members | Listar membros |
| GET | /projects/{id}/members?search= | Buscar membro por nome ou login |
| DELETE | /projects/{id}/members/{userId} | Remover membro |

### Tarefas
| Método | Rota | Descrição |
|---|---|---|
| POST | /projects/{id}/tasks | Criar tarefa |
| GET | /projects/{id}/tasks | Listar tarefas |
| GET | /projects/{id}/tasks?title= | Buscar tarefa por título |
| GET | /projects/{id}/tasks?status= | Filtrar por status |
| GET | /projects/{id}/tasks?assignedTo= | Filtrar por utilizador |
| GET | /projects/{id}/tasks/{taskId} | Buscar tarefa por id |
| PUT | /projects/{id}/tasks/{taskId} | Atualizar tarefa |
| DELETE | /projects/{id}/tasks/{taskId} | Deletar tarefa |