package io.github.ESTECHTI.quarkussocial.domain.repository;

import io.github.ESTECHTI.quarkussocial.domain.model.User;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UserRespository implements PanacheRepository<User> {

}
