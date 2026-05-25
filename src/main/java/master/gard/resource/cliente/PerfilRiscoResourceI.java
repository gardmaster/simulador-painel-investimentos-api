package master.gard.resource.cliente;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import master.gard.dto.response.cliente.PerfilRiscoResponse;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@Path("api/v1/perfil-risco")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Perfil de Risco") // TODO: DEFINIR MELHOR O ENDPOINT NO OPENAPI CONFIG
public interface PerfilRiscoResourceI {

    @GET
    @Path("{clienteId}")
    @RolesAllowed("admin")
    @Operation(
            summary = "Obter perfil de risco por ID",
            description = "Retorna os detalhes de um perfil de risco específico com base no seu ID."
    )
    @APIResponse(
            responseCode = "200",
            description = "Perfil de risco encontrado e retornado com sucesso",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = PerfilRiscoResponse.class)
            )
    )
    @APIResponse(ref = "NotFoundCliente")
    Response getPerfilRiscoByClienteId(
            @Parameter(description = "ID do cliente para o qual o perfil de risco será obtido", required = true, example = "3")
            @PathParam("clienteId") Long clienteId
    );

    @GET
    @Path("/me")
    @RolesAllowed({"admin", "user"})
    @Operation(
            summary = "Obter perfil de risco do cliente autenticado",
            description = "Retorna os detalhes do perfil de risco do cliente atualmente autenticado."
    )
    @APIResponse(
            responseCode = "200",
            description = "Perfil de risco do cliente autenticado encontrado e retornado com sucesso",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = PerfilRiscoResponse.class)
            )
    )
    @APIResponse(ref = "NotFoundCliente")
    Response getPerfilRiscoDoClienteAutenticado();
}
