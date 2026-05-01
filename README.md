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
2. Crie o ficheiro `application-local.properties` com as suas credenciais
3. Execute:
```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=local
```

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
| GET | /projects/{id} | Buscar projeto |
| PUT | /projects/{id} | Atualizar projeto |
| DELETE | /projects/{id} | Deletar projeto |
| POST | /projects/{id}/members | Adicionar membro |
| GET | /projects/{id}/members | Listar membros |
| DELETE | /projects/{id}/members/{userId} | Remover membro |

### Tarefas
| Método | Rota | Descrição |
|---|---|---|
| POST | /projects/{id}/tasks | Criar tarefa |
| GET | /projects/{id}/tasks | Listar tarefas |
| GET | /projects/{id}/tasks/{taskId} | Buscar tarefa |
| PUT | /projects/{id}/tasks/{taskId} | Atualizar tarefa |
| DELETE | /projects/{id}/tasks/{taskId} | Deletar tarefa |