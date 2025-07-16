# Tutorial: Como Refatorar Código Quarkus REST

## 📚 Índice
1. [Análise do Código Atual](#análise-do-código-atual)
2. [Problemas Identificados](#problemas-identificados)
3. [Melhores Práticas](#melhores-práticas)
4. [Refatoração Passo a Passo](#refatoração-passo-a-passo)
5. [Código Final](#código-final)

## 🔍 Análise do Código Atual

### Estrutura Inicial
O projeto atual possui uma estrutura básica do Quarkus com apenas um endpoint:

```java
@Path("/hello")
public class GreetingResource {
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return "Hello RESTEasy";
    }
}
```

### O que funciona:
✅ Projeto compila corretamente  
✅ Testes passam  
✅ Endpoint básico funcionando  
✅ Estrutura Maven configurada  

## ⚠️ Problemas Identificados

### 1. **Falta de Separação de Responsabilidades**
- Tudo está na classe Resource (Controller)
- Não há camada de Service
- Não há modelo de dados

### 2. **Resposta Muito Simples**
- Retorna apenas texto simples
- Não usa JSON (padrão para APIs REST)
- Não tem estrutura de resposta padronizada

### 3. **Falta de Funcionalidades Reais**
- Apenas um endpoint GET
- Não demonstra CRUD completo
- Não há validação de dados

### 4. **Configuração Vazia**
- `application.properties` vazio
- Sem configurações de logging
- Sem configurações de ambiente

## 🏆 Melhores Práticas

### 1. **Arquitetura em Camadas**
```
Controller (Resource) → Service → Repository/Model
```

### 2. **Respostas Padronizadas**
- Usar JSON como formato padrão
- Estrutura consistente de resposta
- Códigos HTTP apropriados

### 3. **Validação de Dados**
- Bean Validation (JSR-303)
- Tratamento de erros
- Mensagens de erro claras

### 4. **Logging e Monitoramento**
- Logs estruturados
- Métricas de performance
- Health checks

## 🔧 Refatoração Passo a Passo

### Passo 1: Melhorar o Endpoint Atual

**Antes:**
```java
@GET
@Produces(MediaType.TEXT_PLAIN)
public String hello() {
    return "Hello RESTEasy";
}
```

**Depois:**
```java
@GET
@Produces(MediaType.APPLICATION_JSON)
public Response hello() {
    Map<String, String> response = Map.of(
        "message", "Hello RESTEasy",
        "timestamp", Instant.now().toString(),
        "version", "1.0"
    );
    return Response.ok(response).build();
}
```

### Passo 2: Criar Modelo de Dados

```java
public class User {
    private Long id;
    private String name;
    private String email;
    private LocalDateTime createdAt;
    
    // construtores, getters, setters
}
```

### Passo 3: Criar Camada de Service

```java
@ApplicationScoped
public class UserService {
    private final List<User> users = new ArrayList<>();
    
    public List<User> findAll() {
        return new ArrayList<>(users);
    }
    
    public User save(User user) {
        user.setId(generateId());
        user.setCreatedAt(LocalDateTime.now());
        users.add(user);
        return user;
    }
}
```

### Passo 4: Criar Resource Completo

```java
@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {
    
    @Inject
    UserService userService;
    
    @GET
    public Response findAll() {
        return Response.ok(userService.findAll()).build();
    }
    
    @POST
    public Response create(@Valid User user) {
        User saved = userService.save(user);
        return Response.status(Response.Status.CREATED)
                .entity(saved).build();
    }
}
```

### Passo 5: Adicionar Validação

```java
public class User {
    @NotNull
    @Size(min = 2, max = 100)
    private String name;
    
    @Email
    @NotNull
    private String email;
}
```

### Passo 6: Configurar application.properties

```properties
# Configurações do servidor
quarkus.http.port=8080

# Logging
quarkus.log.level=INFO
quarkus.log.category."io.github.ESTECHTI".level=DEBUG

# Configurações de desenvolvimento
%dev.quarkus.log.console.enable=true
%dev.quarkus.log.console.format=%d{HH:mm:ss} %-5p [%c{2.}] (%t) %s%e%n
```

## 📋 Exemplo Prático: API de Rede Social

Vamos criar uma API simples para uma rede social com posts e usuários:

### Estrutura Final:
```
src/main/java/io/github/ESTECHTI/
├── model/
│   ├── User.java
│   └── Post.java
├── service/
│   ├── UserService.java
│   └── PostService.java
├── resource/
│   ├── GreetingResource.java (melhorado)
│   ├── UserResource.java
│   └── PostResource.java
└── dto/
    ├── CreateUserRequest.java
    └── CreatePostRequest.java
```

## 🎯 Benefícios da Refatoração

### Antes:
- ❌ Código monolítico
- ❌ Resposta em texto simples
- ❌ Sem validação
- ❌ Sem estrutura

### Depois:
- ✅ Código bem estruturado
- ✅ API REST completa
- ✅ Validação de dados
- ✅ Respostas JSON
- ✅ Separação de responsabilidades
- ✅ Logging configurado
- ✅ Testes abrangentes

## 🚀 Próximos Passos

1. **Persistência**: Adicionar banco de dados (H2, PostgreSQL)
2. **Segurança**: Implementar autenticação JWT
3. **Documentação**: Swagger/OpenAPI
4. **Testes**: Aumentar cobertura de testes
5. **Monitoramento**: Métricas e health checks

## 📖 Recursos Adicionais

- [Guia Oficial do Quarkus](https://quarkus.io/guides/)
- [REST Best Practices](https://restfulapi.net/)
- [Bean Validation](https://beanvalidation.org/)
- [Arquitetura em Camadas](https://martinfowler.com/architecture/)