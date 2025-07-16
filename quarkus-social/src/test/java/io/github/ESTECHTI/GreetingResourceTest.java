package io.github.ESTECHTI;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

/**
 * Testes para o GreetingResource melhorado
 * 
 * Demonstra:
 * - Testes para endpoints JSON
 * - Validação de estrutura de resposta
 * - Testes com parâmetros
 * - Múltiplos cenários de teste
 */
@QuarkusTest
class GreetingResourceTest {

    @Test
    void testHelloEndpoint() {
        given()
          .when().get("/hello")
          .then()
             .statusCode(200)
             .body("message", is("Hello RESTEasy!"))
             .body("version", is("2.0"))
             .body("framework", is("Quarkus"))
             .body("status", is("success"))
             .body("timestamp", notNullValue());
    }

    @Test
    void testHelloWithNameParameter() {
        given()
          .queryParam("name", "João")
          .when().get("/hello")
          .then()
             .statusCode(200)
             .body("message", is("Hello João!"))
             .body("version", is("2.0"))
             .body("framework", is("Quarkus"))
             .body("status", is("success"))
             .body("timestamp", notNullValue());
    }

    @Test
    void testHelloWithEmptyName() {
        given()
          .queryParam("name", "")
          .when().get("/hello")
          .then()
             .statusCode(200)
             .body("message", is("Hello RESTEasy!"))
             .body("status", is("success"));
    }

    @Test
    void testStatusEndpoint() {
        given()
          .when().get("/hello/status")
          .then()
             .statusCode(200)
             .body("status", is("UP"))
             .body("service", is("quarkus-social"))
             .body("version", is("1.0"))
             .body("uptime", is("Aplicação rodando"))
             .body("timestamp", notNullValue());
    }
}