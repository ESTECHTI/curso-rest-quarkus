package io.github.ESTECHTI.service;

import io.github.ESTECHTI.model.User;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

/**
 * Serviço para gerenciar usuários
 * 
 * Demonstra:
 * - Separação de responsabilidades
 * - Injeção de dependência com @ApplicationScoped
 * - Simulação de persistência em memória
 * - Logging estruturado
 * - Validações de negócio
 */
@ApplicationScoped
public class UserService {

    private static final Logger LOGGER = Logger.getLogger(UserService.class.getName());
    
    // Simulação de banco de dados em memória
    private final List<User> users = new ArrayList<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    /**
     * Construtor que inicializa alguns dados de exemplo
     */
    public UserService() {
        initializeSampleData();
    }

    /**
     * Busca todos os usuários ativos
     * 
     * @return Lista de usuários ativos
     */
    public List<User> findAllActive() {
        LOGGER.info("Buscando todos os usuários ativos");
        List<User> activeUsers = users.stream()
                .filter(User::isActive)
                .toList();
        LOGGER.info("Encontrados " + activeUsers.size() + " usuários ativos");
        return activeUsers;
    }

    /**
     * Busca usuário por ID
     * 
     * @param id ID do usuário
     * @return Optional contendo o usuário se encontrado
     */
    public Optional<User> findById(Long id) {
        LOGGER.info("Buscando usuário com ID: " + id);
        Optional<User> user = users.stream()
                .filter(u -> u.getId().equals(id))
                .findFirst();
        
        if (user.isPresent()) {
            LOGGER.info("Usuário encontrado: " + user.get().getUsername());
        } else {
            LOGGER.warning("Usuário não encontrado com ID: " + id);
        }
        
        return user;
    }

    /**
     * Busca usuário por username
     * 
     * @param username Username do usuário
     * @return Optional contendo o usuário se encontrado
     */
    public Optional<User> findByUsername(String username) {
        LOGGER.info("Buscando usuário com username: " + username);
        return users.stream()
                .filter(u -> u.getUsername().equalsIgnoreCase(username))
                .findFirst();
    }

    /**
     * Cria um novo usuário
     * 
     * @param user Dados do usuário
     * @return Usuário criado com ID gerado
     * @throws IllegalArgumentException se os dados forem inválidos
     */
    public User create(User user) {
        LOGGER.info("Criando novo usuário: " + user.getUsername());
        
        // Validações de negócio
        validateUser(user);
        validateUniqueUsername(user.getUsername());
        validateUniqueEmail(user.getEmail());
        
        // Gerar ID e salvar
        user.setId(idGenerator.getAndIncrement());
        users.add(user);
        
        LOGGER.info("Usuário criado com sucesso - ID: " + user.getId() + ", Username: " + user.getUsername());
        return user;
    }

    /**
     * Atualiza um usuário existente
     * 
     * @param id ID do usuário
     * @param updatedUser Dados atualizados
     * @return Optional contendo o usuário atualizado
     */
    public Optional<User> update(Long id, User updatedUser) {
        LOGGER.info("Atualizando usuário com ID: " + id);
        
        Optional<User> existingUser = findById(id);
        if (existingUser.isEmpty()) {
            LOGGER.warning("Tentativa de atualizar usuário inexistente - ID: " + id);
            return Optional.empty();
        }

        User user = existingUser.get();
        
        // Atualizar apenas campos não nulos
        if (updatedUser.getName() != null) {
            user.setName(updatedUser.getName());
        }
        if (updatedUser.getEmail() != null) {
            validateUniqueEmailForUpdate(updatedUser.getEmail(), id);
            user.setEmail(updatedUser.getEmail());
        }
        
        LOGGER.info("Usuário atualizado com sucesso - ID: " + id);
        return Optional.of(user);
    }

    /**
     * Desativa um usuário (soft delete)
     * 
     * @param id ID do usuário
     * @return true se o usuário foi desativado, false se não encontrado
     */
    public boolean deactivate(Long id) {
        LOGGER.info("Desativando usuário com ID: " + id);
        
        Optional<User> user = findById(id);
        if (user.isPresent()) {
            user.get().setActive(false);
            LOGGER.info("Usuário desativado com sucesso - ID: " + id);
            return true;
        }
        
        LOGGER.warning("Tentativa de desativar usuário inexistente - ID: " + id);
        return false;
    }

    /**
     * Conta total de usuários ativos
     * 
     * @return Número de usuários ativos
     */
    public long countActiveUsers() {
        return users.stream().filter(User::isActive).count();
    }

    // Métodos privados de validação e utilitários

    private void validateUser(User user) {
        if (user.getName() == null || user.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome é obrigatório");
        }
        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email é obrigatório");
        }
        if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("Username é obrigatório");
        }
        if (user.getName().length() < 2) {
            throw new IllegalArgumentException("Nome deve ter pelo menos 2 caracteres");
        }
        if (user.getUsername().length() < 3) {
            throw new IllegalArgumentException("Username deve ter pelo menos 3 caracteres");
        }
        if (!user.getEmail().contains("@")) {
            throw new IllegalArgumentException("Email deve ter formato válido");
        }
    }

    private void validateUniqueUsername(String username) {
        if (findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("Username já existe: " + username);
        }
    }

    private void validateUniqueEmail(String email) {
        if (users.stream().anyMatch(u -> u.getEmail().equalsIgnoreCase(email))) {
            throw new IllegalArgumentException("Email já existe: " + email);
        }
    }

    private void validateUniqueEmailForUpdate(String email, Long userId) {
        if (users.stream().anyMatch(u -> u.getEmail().equalsIgnoreCase(email) && !u.getId().equals(userId))) {
            throw new IllegalArgumentException("Email já existe: " + email);
        }
    }

    private void initializeSampleData() {
        LOGGER.info("Inicializando dados de exemplo");
        
        // Criar alguns usuários de exemplo
        create(new User("João Silva", "joao@email.com", "joaosilva"));
        create(new User("Maria Santos", "maria@email.com", "mariasantos"));
        create(new User("Pedro Oliveira", "pedro@email.com", "pedrooliveira"));
        
        LOGGER.info("Dados de exemplo criados - Total de usuários: " + users.size());
    }
}