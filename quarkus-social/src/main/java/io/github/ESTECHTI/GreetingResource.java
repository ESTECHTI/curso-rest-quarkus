package io.github.ESTECHTI;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.time.Instant;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Resource de exemplo melhorado com boas práticas
 * 
 * Melhorias implementadas:
 * - Resposta em JSON ao invés de texto simples
 * - Logging estruturado
 * - Parâmetros opcionais
 * - Códigos de status HTTP apropriados
 * - Documentação JavaDoc
 */
@Path("/hello")
@Produces(MediaType.APPLICATION_JSON)
public class GreetingResource {

    private static final Logger LOGGER = Logger.getLogger(GreetingResource.class.getName());

    /**
     * Endpoint de saudação melhorado
     * 
     * @param name Nome opcional para personalizar a saudação
     * @return Response JSON com informações estruturadas
     */
    @GET
    public Response hello(@QueryParam("name") String name) {
        LOGGER.info("Processando requisição de saudação" + (name != null ? " para: " + name : ""));
        
        String message = name != null && !name.trim().isEmpty() 
            ? "Hello " + name + "!" 
            : "Hello RESTEasy!";
        
        Map<String, Object> response = Map.of(
            "message", message,
            "timestamp", Instant.now().toString(),
            "version", "2.0",
            "framework", "Quarkus",
            "status", "success"
        );
        
        LOGGER.info("Saudação processada com sucesso");
        return Response.ok(response).build();
    }

    /**
     * Endpoint de status da aplicação
     * 
     * @return Status da aplicação
     */
    @GET
    @Path("/status")
    public Response status() {
        LOGGER.info("Verificando status da aplicação");
        
        Map<String, Object> status = Map.of(
            "status", "UP",
            "service", "quarkus-social",
            "version", "1.0",
            "timestamp", Instant.now().toString(),
            "uptime", "Aplicação rodando"
        );
        
        return Response.ok(status).build();
    }
}
