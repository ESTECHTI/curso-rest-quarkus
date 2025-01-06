package io.github.ESTECHTI.quarkussocial.rest;

import io.github.ESTECHTI.quarkussocial.domain.model.User;
import io.github.ESTECHTI.quarkussocial.domain.repository.UserRespository;
import io.github.ESTECHTI.quarkussocial.rest.dto.CreatePostRequest;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import jakarta.ws.rs.core.Response;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.mockito.Mockito;

import static io.restassured.RestAssured.given;
import static org.mockito.Mockito.*;

@QuarkusTest
@TestHTTPEndpoint(PostResource.class)
class PostResourceTest {

    @Inject
    UserRespository userRespository;
    PostResource postResource;
    Long userId;

    @BeforeEach
    @Transactional
    public void setUP() {
        var user = new User();
        user.setAge(30);
        user.setName("Fulano");
        userRespository.persist(user);
        userId = user.getId();
    }

    @Test
    @DisplayName("should create a post for a user")
    public void createPostTest() {
        var postRequest = new CreatePostRequest();
        postRequest.setText("Some text");

        given()
            .contentType(ContentType.JSON)
            .body(postRequest)
            .pathParam("userId", userId)
        .when()
            .post()
        .then()
                .statusCode(201);
    }

    @Test
    @DisplayName("should return 404 when trying to make a post for an inexistent user")
    public void postForAnInexistentUserTest() {
        var postRequest = new CreatePostRequest();
        postRequest.setText("Some text");

        var inexistentUserId = 999;

        given()
                .contentType(ContentType.JSON)
                .body(postRequest)
                .pathParam("userId", inexistentUserId)
            .when()
                .post()
            .then()
                .statusCode(404);

    }

    @Test
    @DisplayName("should return 404 when user doesn't exist")
    public void listPostUserNotFoundTest() {

    }

    @Test
    @DisplayName("should return 400 when follower header is not present")
    public void listPostFollowerHeaderNotSendTest() {

    }

    @Test
    @DisplayName("should return 400 when follower doesn't exist")
    public void listPostFollowerHeaderNotFoundTest() {

    }

    @Test
    @DisplayName("should return 400 when follower isn't a follower")
    public void listPostNotAFollower() {

    }

    @Test
    @DisplayName("should return posts")
    public void listPostTest() {

    }
}