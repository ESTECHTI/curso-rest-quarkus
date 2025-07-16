package io.github.ESTECHTI.resource;

import io.github.ESTECHTI.model.User;
import io.github.ESTECHTI.service.UserService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * Resource (Controller) para gerenciar usuários
 * 
 * Demonstra:
 * - API REST completa (CRUD)
 * - Injeção de dependência
 * - Tratamento de erros
 * - Códigos de status HTTP apropriados
 * - Documentação clara dos endpoints
 * - Separação de responsabilidades (Resource -> Service)
 */
@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {

    private static final Logger LOGGER = Logger.getLogger(UserResource.class.getName());

    @Inject
    UserService userService;

    /**
     * Lista todos os usuários ativos
     * 
     * GET /users
     * 
     * @return Lista de usuários com status 200
     */
    @GET
    public Response findAll() {
        LOGGER.info("Endpoint GET /users chamado");
        
        try {
            var users = userService.findAllActive();
            
            Map<String, Object> response = Map.of(
                "users", users,
                "total", users.size(),
                "status", "success"
            );
            
            return Response.ok(response).build();
            
        } catch (Exception e) {
            LOGGER.severe("Erro ao buscar usuários: " + e.getMessage());
            return createErrorResponse("Erro interno do servidor", Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Busca usuário por ID
     * 
     * GET /users/{id}
     * 
     * @param id ID do usuário
     * @return Usuário encontrado (200) ou erro 404
     */
    @GET
    @Path("/{id}")
    public Response findById(@PathParam("id") Long id) {
        LOGGER.info("Endpoint GET /users/" + id + " chamado");
        
        if (id == null || id <= 0) {
            return createErrorResponse("ID deve ser um número positivo", Response.Status.BAD_REQUEST);
        }
        
        try {
            Optional<User> user = userService.findById(id);
            
            if (user.isPresent()) {
                Map<String, Object> response = Map.of(
                    "user", user.get(),
                    "status", "success"
                );
                return Response.ok(response).build();
            } else {
                return createErrorResponse("Usuário não encontrado", Response.Status.NOT_FOUND);
            }
            
        } catch (Exception e) {
            LOGGER.severe("Erro ao buscar usuário por ID: " + e.getMessage());
            return createErrorResponse("Erro interno do servidor", Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Cria um novo usuário
     * 
     * POST /users
     * 
     * @param user Dados do usuário
     * @return Usuário criado (201) ou erro de validação (400)
     */
    @POST
    public Response create(User user) {
        LOGGER.info("Endpoint POST /users chamado");
        
        if (user == null) {
            return createErrorResponse("Dados do usuário são obrigatórios", Response.Status.BAD_REQUEST);
        }
        
        try {
            User createdUser = userService.create(user);
            
            Map<String, Object> response = Map.of(
                "user", createdUser,
                "message", "Usuário criado com sucesso",
                "status", "success"
            );
            
            return Response.status(Response.Status.CREATED).entity(response).build();
            
        } catch (IllegalArgumentException e) {
            LOGGER.warning("Erro de validação ao criar usuário: " + e.getMessage());
            return createErrorResponse(e.getMessage(), Response.Status.BAD_REQUEST);
            
        } catch (Exception e) {
            LOGGER.severe("Erro interno ao criar usuário: " + e.getMessage());
            return createErrorResponse("Erro interno do servidor", Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Atualiza um usuário existente
     * 
     * PUT /users/{id}
     * 
     * @param id ID do usuário
     * @param updatedUser Dados atualizados
     * @return Usuário atualizado (200) ou erro 404/400
     */
    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") Long id, User updatedUser) {
        LOGGER.info("Endpoint PUT /users/" + id + " chamado");
        
        if (id == null || id <= 0) {
            return createErrorResponse("ID deve ser um número positivo", Response.Status.BAD_REQUEST);
        }
        
        if (updatedUser == null) {
            return createErrorResponse("Dados do usuário são obrigatórios", Response.Status.BAD_REQUEST);
        }
        
        try {
            Optional<User> user = userService.update(id, updatedUser);
            
            if (user.isPresent()) {
                Map<String, Object> response = Map.of(
                    "user", user.get(),
                    "message", "Usuário atualizado com sucesso",
                    "status", "success"
                );
                return Response.ok(response).build();
            } else {
                return createErrorResponse("Usuário não encontrado", Response.Status.NOT_FOUND);
            }
            
        } catch (IllegalArgumentException e) {
            LOGGER.warning("Erro de validação ao atualizar usuário: " + e.getMessage());
            return createErrorResponse(e.getMessage(), Response.Status.BAD_REQUEST);
            
        } catch (Exception e) {
            LOGGER.severe("Erro interno ao atualizar usuário: " + e.getMessage());
            return createErrorResponse("Erro interno do servidor", Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Desativa um usuário (soft delete)
     * 
     * DELETE /users/{id}
     * 
     * @param id ID do usuário
     * @return Confirmação (204) ou erro 404
     */
    @DELETE
    @Path("/{id}")
    public Response deactivate(@PathParam("id") Long id) {
        LOGGER.info("Endpoint DELETE /users/" + id + " chamado");
        
        if (id == null || id <= 0) {
            return createErrorResponse("ID deve ser um número positivo", Response.Status.BAD_REQUEST);
        }
        
        try {
            boolean deactivated = userService.deactivate(id);
            
            if (deactivated) {
                Map<String, Object> response = Map.of(
                    "message", "Usuário desativado com sucesso",
                    "status", "success"
                );
                return Response.ok(response).build();
            } else {
                return createErrorResponse("Usuário não encontrado", Response.Status.NOT_FOUND);
            }
            
        } catch (Exception e) {
            LOGGER.severe("Erro interno ao desativar usuário: " + e.getMessage());
            return createErrorResponse("Erro interno do servidor", Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Busca usuário por username
     * 
     * GET /users/search?username={username}
     * 
     * @param username Username para busca
     * @return Usuário encontrado ou erro 404
     */
    @GET
    @Path("/search")
    public Response findByUsername(@QueryParam("username") String username) {
        LOGGER.info("Endpoint GET /users/search chamado com username: " + username);
        
        if (username == null || username.trim().isEmpty()) {
            return createErrorResponse("Parâmetro username é obrigatório", Response.Status.BAD_REQUEST);
        }
        
        try {
            Optional<User> user = userService.findByUsername(username);
            
            if (user.isPresent()) {
                Map<String, Object> response = Map.of(
                    "user", user.get(),
                    "status", "success"
                );
                return Response.ok(response).build();
            } else {
                return createErrorResponse("Usuário não encontrado", Response.Status.NOT_FOUND);
            }
            
        } catch (Exception e) {
            LOGGER.severe("Erro ao buscar usuário por username: " + e.getMessage());
            return createErrorResponse("Erro interno do servidor", Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Retorna estatísticas dos usuários
     * 
     * GET /users/stats
     * 
     * @return Estatísticas dos usuários
     */
    @GET
    @Path("/stats")
    public Response getStats() {
        LOGGER.info("Endpoint GET /users/stats chamado");
        
        try {
            long totalActive = userService.countActiveUsers();
            
            Map<String, Object> stats = Map.of(
                "totalActiveUsers", totalActive,
                "status", "success"
            );
            
            return Response.ok(stats).build();
            
        } catch (Exception e) {
            LOGGER.severe("Erro ao buscar estatísticas: " + e.getMessage());
            return createErrorResponse("Erro interno do servidor", Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Método utilitário para criar respostas de erro padronizadas
     */
    private Response createErrorResponse(String message, Response.Status status) {
        Map<String, Object> error = Map.of(
            "error", message,
            "status", "error",
            "code", status.getStatusCode()
        );
        return Response.status(status).entity(error).build();
    }
}