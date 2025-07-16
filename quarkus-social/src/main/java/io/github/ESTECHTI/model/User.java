package io.github.ESTECHTI.model;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Modelo representando um usuário da rede social
 * 
 * Demonstra:
 * - Encapsulamento adequado
 * - Construtores apropriados
 * - Métodos equals/hashCode
 * - Documentação clara
 */
public class User {
    
    private Long id;
    private String name;
    private String email;
    private String username;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private int followersCount;
    private int followingCount;
    private boolean active;

    // Construtor padrão
    public User() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.active = true;
        this.followersCount = 0;
        this.followingCount = 0;
    }

    // Construtor com campos obrigatórios
    public User(String name, String email, String username) {
        this();
        this.name = name;
        this.email = email;
        this.username = username;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        this.updatedAt = LocalDateTime.now();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
        this.updatedAt = LocalDateTime.now();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
        this.updatedAt = LocalDateTime.now();
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public int getFollowersCount() {
        return followersCount;
    }

    public void setFollowersCount(int followersCount) {
        this.followersCount = followersCount;
    }

    public int getFollowingCount() {
        return followingCount;
    }

    public void setFollowingCount(int followingCount) {
        this.followingCount = followingCount;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
        this.updatedAt = LocalDateTime.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) &&
               Objects.equals(username, user.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", username='" + username + '\'' +
                ", active=" + active +
                '}';
    }
}