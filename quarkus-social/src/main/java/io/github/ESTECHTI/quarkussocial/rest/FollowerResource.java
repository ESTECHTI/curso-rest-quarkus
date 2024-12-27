package io.github.ESTECHTI.quarkussocial.rest;

import io.github.ESTECHTI.quarkussocial.domain.model.Follower;
import io.github.ESTECHTI.quarkussocial.domain.model.User;
import io.github.ESTECHTI.quarkussocial.domain.repository.FollowRespository;
import io.github.ESTECHTI.quarkussocial.domain.repository.UserRespository;
import io.github.ESTECHTI.quarkussocial.rest.dto.FollowerRequest;
import io.github.ESTECHTI.quarkussocial.rest.dto.FollowerResponse;
import io.github.ESTECHTI.quarkussocial.rest.dto.FollowersPerUserResponse;
import io.github.ESTECHTI.quarkussocial.rest.dto.ResponseError;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.stream.Collectors;

@Path("/users/{userId}/followers")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class FollowerResource {

    private final FollowRespository repository;
    private final UserRespository userRespository;

    @Inject
    public FollowerResource(
            FollowRespository repository, UserRespository userRespository) {
        this.repository = repository;
        this.userRespository = userRespository;
    }

    @PUT
    @Transactional
    public Response followUser(
            @PathParam("userId") Long userId, FollowerRequest request) {

        if(userId.equals(request.getFollowerId())){
            return Response.status(Response.Status.CONFLICT)
                    .entity("You can't follow yourself")
                    .build();
        }

        var user = userRespository.findById(userId);
        if(user == null){
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        var follower = userRespository.findById(request.getFollowerId());
        if (follower == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Follower not found")
                    .build();
        }
        boolean follows = repository.follows(follower, user);

        if(!follows){
            var entity = new Follower();
            entity.setUser(user);
            entity.setFollower(follower);

            repository.persist(entity);
        }

        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @GET
    public Response listFollowers(@PathParam("userId") Long userId) {

        var user = userRespository.findById(userId);
        if(user == null){
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        var list = repository.findByUser(userId);
        FollowersPerUserResponse responseObject = new FollowersPerUserResponse();
        responseObject.setFollowersCount(list.size());

        var followerList = list.stream().map( FollowerResponse::new ).collect(Collectors.toList());

        responseObject.setContent(followerList);
        return Response.ok(responseObject).build();
    }
}
