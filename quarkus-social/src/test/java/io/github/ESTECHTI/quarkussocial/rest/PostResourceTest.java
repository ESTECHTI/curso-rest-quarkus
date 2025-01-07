package io.github.ESTECHTI.quarkussocial.rest;

import io.github.ESTECHTI.quarkussocial.domain.model.Follower;
import io.github.ESTECHTI.quarkussocial.domain.model.Post;
import io.github.ESTECHTI.quarkussocial.domain.model.User;
import io.github.ESTECHTI.quarkussocial.domain.repository.FollowRespository;
import io.github.ESTECHTI.quarkussocial.domain.repository.PostRepository;
import io.github.ESTECHTI.quarkussocial.domain.repository.UserRespository;
import io.github.ESTECHTI.quarkussocial.rest.dto.CreatePostRequest;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

@QuarkusTest
@TestHTTPEndpoint(PostResource.class)
class PostResourceTest {

    @Inject
    UserRespository userRespository;
    @Inject
    FollowRespository followRespository;
    @Inject
    PostRepository postRepository;
    Long userId;
    Long userNotFollowerId;
    Long userFollowerId;

    @BeforeEach
    @Transactional
    public void setUP() {
        //usuario padrão dos testes
        var user = new User();
        user.setAge(30);
        user.setName("Fulano");
        userRespository.persist(user);
        userId = user.getId();

        //criada a postagem para o usuário
        Post post = new Post();
        post.setText("Hello");
        post.setUser(user);
        postRepository.persist(post);

        //usuario que não segue ninguém
        var userNotFollower = new User();
        user.setAge(33);
        user.setName("Ciclano");
        userRespository.persist(userNotFollower);
        userNotFollowerId = userNotFollower.getId();

        //usuário seguidor
        var userFollower = new User();
        userFollower.setAge(31);
        userFollower.setName("Beltrano");
        userRespository.persist(userFollower);
        userFollowerId = userFollower.getId();

        Follower follower = new Follower();
        follower.setUser(user);
        follower.setFollower(userFollower);
        followRespository.persist(follower);
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
        var inexistentUserId = 999;

        given()
            .pathParam("userId", inexistentUserId)
        .when()
            .get()
        .then()
            .statusCode(404);

    }

    @Test
    @DisplayName("should return 400 when follower header is not present")
    public void listPostFollowerHeaderNotSendTest() {
        given()
            .pathParam("userId", userId)
        .when()
            .get()
        .then()
            .statusCode(400)
                .body(Matchers.is("You forgot the header followerId"));
    }

    @Test
    @DisplayName("should return 400 when follower doesn't exist")
    public void listPostFollowerHeaderNotFoundTest() {
        var inexistentFollowerId = 999;
        given()
            .pathParam("userId", userId)
                .header("followerId", inexistentFollowerId)
        .when()
            .get()
        .then()
            .statusCode(400)
            .body(Matchers.is("Inexistent followerId"));
    }

    @Test
    @DisplayName("should return 403 when follower isn't a follower")
    public void listPostNotAFollower() {
        given()
            .pathParam("userId", userId)
            .header("followerId", userNotFollowerId)
        .when()
            .get()
        .then()
            .statusCode(403)
            .body(Matchers.is("You can't see these posts"));
    }

    @Test
    @DisplayName("should return posts")
    public void listPostTest() {
        given()
            .pathParam("userId", userId)
            .header("followerId", userFollowerId)
        .when()
            .get()
        .then()
            .statusCode(200)
            .body("size()", Matchers.is(1));
    }
}