# 🎯 Demonstração Prática da API Refatorada

Este script demonstra todas as funcionalidades implementadas na refatoração do projeto Quarkus Social.

## 📦 Executar a Aplicação

```bash
# Iniciar em modo de desenvolvimento
./mvnw compile quarkus:dev

# Ou usar Maven diretamente
mvn compile quarkus:dev
```

## 🔍 Testando os Endpoints

### 1. **Endpoint Hello Melhorado**

```bash
# Saudação simples
curl -X GET http://localhost:8080/hello

# Saudação personalizada
curl -X GET "http://localhost:8080/hello?name=João"

# Status da aplicação
curl -X GET http://localhost:8080/hello/status
```

### 2. **API de Usuários - CRUD Completo**

#### Listar todos os usuários
```bash
curl -X GET http://localhost:8080/users
```

#### Buscar usuário por ID
```bash
curl -X GET http://localhost:8080/users/1
```

#### Buscar usuário por username
```bash
curl -X GET "http://localhost:8080/users/search?username=joaosilva"
```

#### Criar novo usuário
```bash
curl -X POST http://localhost:8080/users \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Ana Silva",
    "email": "ana@email.com", 
    "username": "anasilva"
  }'
```

#### Atualizar usuário
```bash
curl -X PUT http://localhost:8080/users/1 \
  -H "Content-Type: application/json" \
  -d '{
    "name": "João Silva Santos",
    "email": "joao.santos@email.com"
  }'
```

#### Desativar usuário
```bash
curl -X DELETE http://localhost:8080/users/3
```

#### Estatísticas dos usuários
```bash
curl -X GET http://localhost:8080/users/stats
```

## 🧪 Executar Testes

```bash
# Executar todos os testes
mvn test

# Executar testes com mais detalhes
mvn test -Dquarkus.log.level=DEBUG
```

## 📈 Exemplos de Resposta

### GET /users
```json
{
  "users": [
    {
      "id": 1,
      "name": "João Silva",
      "email": "joao@email.com",
      "username": "joaosilva",
      "createdAt": "2024-01-01T10:00:00",
      "updatedAt": "2024-01-01T10:00:00",
      "followersCount": 0,
      "followingCount": 0,
      "active": true
    }
  ],
  "total": 3,
  "status": "success"
}
```

### POST /users (Sucesso)
```json
{
  "user": {
    "id": 4,
    "name": "Ana Silva",
    "email": "ana@email.com",
    "username": "anasilva",
    "createdAt": "2024-01-01T10:30:00",
    "updatedAt": "2024-01-01T10:30:00",
    "followersCount": 0,
    "followingCount": 0,
    "active": true
  },
  "message": "Usuário criado com sucesso",
  "status": "success"
}
```

### Erro de Validação
```json
{
  "error": "Username já existe: joaosilva",
  "status": "error",
  "code": 400
}
```

## 🔧 Testando Cenários de Erro

### Criar usuário com dados inválidos
```bash
curl -X POST http://localhost:8080/users \
  -H "Content-Type: application/json" \
  -d '{
    "name": "",
    "email": "email-invalido",
    "username": "ab"
  }'
```

### Buscar usuário inexistente
```bash
curl -X GET http://localhost:8080/users/999
```

### Tentar criar usuário com username duplicado
```bash
curl -X POST http://localhost:8080/users \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Outro João",
    "email": "outro@email.com",
    "username": "joaosilva"
  }'
```

## 📊 Monitoramento

### Logs da aplicação
```bash
# Os logs mostram:
# - Todas as requisições recebidas
# - Validações realizadas
# - Operações executadas
# - Erros tratados
```

### Exemplo de log:
```
17:04:20 INFO [io.gi.ES.re.UserResource] Endpoint GET /users chamado
17:04:20 INFO [io.gi.ES.se.UserService] Buscando todos os usuários ativos
17:04:20 INFO [io.gi.ES.se.UserService] Encontrados 3 usuários ativos
```

## 🎨 Configurações Demonstradas

- **CORS habilitado** para desenvolvimento
- **Logging configurado** por ambiente (dev, test, prod)
- **Respostas JSON padronizadas**
- **Tratamento de erros consistente**
- **Validações de negócio**
- **Separação de responsabilidades**

## 🚀 Próximos Passos Sugeridos

1. **Banco de Dados**: Substituir lista em memória por JPA + H2/PostgreSQL
2. **Validação Bean Validation**: Adicionar @Valid, @NotNull, @Email
3. **Paginação**: Implementar paginação nos endpoints de listagem
4. **Segurança**: Adicionar autenticação JWT
5. **Documentação**: Integrar Swagger/OpenAPI
6. **Testes**: Adicionar testes de integração com TestContainers
7. **Posts**: Implementar entidade Post para completar a rede social