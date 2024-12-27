package io.github.ESTECHTI.quarkussocial.domain.model;

import io.smallrye.common.constraint.NotNull;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "followers")
@Data
public class Follower {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;


    @ManyToOne
    @JoinColumn(name = "follower_id", nullable = false)
    private User follower;
}
