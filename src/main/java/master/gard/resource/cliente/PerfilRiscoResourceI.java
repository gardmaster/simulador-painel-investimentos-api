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
    @Path("{id}")
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
    Response getPerfilRiscoById(
            @Parameter(description = "ID do cliente para o qual o perfil de risco será obtido", required = true, example = "3")
            @PathParam("id") Long id
    );

}
