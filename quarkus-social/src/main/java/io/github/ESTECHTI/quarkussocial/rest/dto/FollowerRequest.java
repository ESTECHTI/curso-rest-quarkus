package io.github.ESTECHTI.quarkussocial.rest.dto;

import jakarta.ws.rs.Consumes;
import lombok.Data;

@Data
public class FollowerRequest {
    private Long followerId;
}
