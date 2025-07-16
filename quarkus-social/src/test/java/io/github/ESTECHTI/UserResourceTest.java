package io.github.ESTECHTI;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;

/**
 * Testes para UserResource
 * 
 * Demonstra:
 * - Testes para API REST completa
 * - Validação de JSON
 * - Testes de CRUD
 * - Cenários de erro
 * - Ordem de execução de testes
 */
@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserResourceTest {

    @Test
    @Order(1)
    void testFindAllUsers() {
        given()
          .when().get("/users")
          .then()
             .statusCode(200)
             .body("status", is("success"))
             .body("users", notNullValue())
             .body("total", greaterThanOrEqualTo(3)) // Dados de exemplo criados no service
             .body("users[0].name", notNullValue())
             .body("users[0].email", notNullValue())
             .body("users[0].username", notNullValue());
    }

    @Test
    @Order(2)
    void testFindUserById() {
        given()
          .when().get("/users/1")
          .then()
             .statusCode(200)
             .body("status", is("success"))
             .body("user.id", is(1))
             .body("user.name", is("João Silva"))
             .body("user.email", is("joao@email.com"))
             .body("user.username", is("joaosilva"))
             .body("user.active", is(true));
    }

    @Test
    @Order(3)
    void testFindUserByIdNotFound() {
        given()
          .when().get("/users/999")
          .then()
             .statusCode(404)
             .body("status", is("error"))
             .body("error", is("Usuário não encontrado"))
             .body("code", is(404));
    }

    @Test
    @Order(4)
    void testFindUserByIdInvalid() {
        given()
          .when().get("/users/0")
          .then()
             .statusCode(400)
             .body("status", is("error"))
             .body("error", is("ID deve ser um número positivo"));
    }

    @Test
    @Order(5)
    void testCreateUser() {
        String newUser = """
            {
                "name": "Ana Costa",
                "email": "ana@email.com",
                "username": "anacosta"
            }
            """;

        given()
          .contentType("application/json")
          .body(newUser)
          .when().post("/users")
          .then()
             .statusCode(201)
             .body("status", is("success"))
             .body("message", is("Usuário criado com sucesso"))
             .body("user.name", is("Ana Costa"))
             .body("user.email", is("ana@email.com"))
             .body("user.username", is("anacosta"))
             .body("user.id", notNullValue())
             .body("user.active", is(true))
             .body("user.createdAt", notNullValue());
    }

    @Test
    @Order(6)
    void testCreateUserWithDuplicateUsername() {
        String duplicateUser = """
            {
                "name": "João Duplicado",
                "email": "joao2@email.com",
                "username": "joaosilva"
            }
            """;

        given()
          .contentType("application/json")
          .body(duplicateUser)
          .when().post("/users")
          .then()
             .statusCode(400)
             .body("status", is("error"))
             .body("error", containsString("Username já existe"));
    }

    @Test
    @Order(7)
    void testCreateUserWithInvalidData() {
        String invalidUser = """
            {
                "name": "",
                "email": "invalid-email",
                "username": "ab"
            }
            """;

        given()
          .contentType("application/json")
          .body(invalidUser)
          .when().post("/users")
          .then()
             .statusCode(400)
             .body("status", is("error"));
    }

    @Test
    @Order(8)
    void testCreateUserWithoutBody() {
        given()
          .contentType("application/json")
          .when().post("/users")
          .then()
             .statusCode(400)
             .body("status", is("error"))
             .body("error", is("Dados do usuário são obrigatórios"));
    }

    @Test
    @Order(9)
    void testUpdateUser() {
        String updatedUser = """
            {
                "name": "João Silva Santos",
                "email": "joao.santos@email.com"
            }
            """;

        given()
          .contentType("application/json")
          .body(updatedUser)
          .when().put("/users/1")
          .then()
             .statusCode(200)
             .body("status", is("success"))
             .body("message", is("Usuário atualizado com sucesso"))
             .body("user.name", is("João Silva Santos"))
             .body("user.email", is("joao.santos@email.com"))
             .body("user.username", is("joaosilva")) // Username não deve mudar
             .body("user.updatedAt", notNullValue());
    }

    @Test
    @Order(10)
    void testUpdateUserNotFound() {
        String updatedUser = """
            {
                "name": "Usuário Inexistente"
            }
            """;

        given()
          .contentType("application/json")
          .body(updatedUser)
          .when().put("/users/999")
          .then()
             .statusCode(404)
             .body("status", is("error"))
             .body("error", is("Usuário não encontrado"));
    }

    @Test
    @Order(11)
    void testFindByUsername() {
        given()
          .queryParam("username", "mariasantos")
          .when().get("/users/search")
          .then()
             .statusCode(200)
             .body("status", is("success"))
             .body("user.username", is("mariasantos"))
             .body("user.name", is("Maria Santos"))
             .body("user.email", is("maria@email.com"));
    }

    @Test
    @Order(12)
    void testFindByUsernameNotFound() {
        given()
          .queryParam("username", "usuarioineistente")
          .when().get("/users/search")
          .then()
             .statusCode(404)
             .body("status", is("error"))
             .body("error", is("Usuário não encontrado"));
    }

    @Test
    @Order(13)
    void testFindByUsernameWithoutParameter() {
        given()
          .when().get("/users/search")
          .then()
             .statusCode(400)
             .body("status", is("error"))
             .body("error", is("Parâmetro username é obrigatório"));
    }

    @Test
    @Order(14)
    void testGetStats() {
        given()
          .when().get("/users/stats")
          .then()
             .statusCode(200)
             .body("status", is("success"))
             .body("totalActiveUsers", greaterThanOrEqualTo(4)); // 3 iniciais + 1 criado
    }

    @Test
    @Order(15)
    void testDeactivateUser() {
        given()
          .when().delete("/users/3")
          .then()
             .statusCode(200)
             .body("status", is("success"))
             .body("message", is("Usuário desativado com sucesso"));
    }

    @Test
    @Order(16)
    void testDeactivateUserNotFound() {
        given()
          .when().delete("/users/999")
          .then()
             .statusCode(404)
             .body("status", is("error"))
             .body("error", is("Usuário não encontrado"));
    }

    @Test
    @Order(17)
    void testStatsAfterDeactivation() {
        given()
          .when().get("/users/stats")
          .then()
             .statusCode(200)
             .body("status", is("success"))
             .body("totalActiveUsers", is(3)); // Um usuário foi desativado
    }
}