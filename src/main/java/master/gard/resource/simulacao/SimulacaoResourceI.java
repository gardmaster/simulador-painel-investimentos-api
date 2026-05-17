package master.gard.resource.simulacao;

import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import master.gard.dto.request.simulacao.SimulacaoFiltroRequest;
import master.gard.dto.request.simulacao.SimulacaoRequest;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@Path("api/v1/simulacoes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Simulações") // TODO: ADICIONAR TAG DESCRITA NO OPENAPICONFIG
public interface SimulacaoResourceI {

    @GET
    @RolesAllowed("admin")
    @Operation(
            summary = "Listar simulações",
            description = "Retorna uma lista de simulações paginadas, permitindo filtros por data, produto e cliente."
    )
    @APIResponse(
            responseCode = "200",
            description = "Simulações retornadas com sucesso"
    )
    @APIResponse(ref = "BadRequest")
    Response listarSimulacoes(@BeanParam SimulacaoFiltroRequest filtro);

    @POST
    @Path("/simular-investimento")
    @RolesAllowed({"admin", "user"})
    @Operation(
            summary = "Simular Investimento",
            description = "Realiza a simulação de um investimento baseado no tipo do produto informado."
    )
    @APIResponse(
            responseCode = "200",
            description = "Simulação realizada com sucesso"
    )
    @APIResponse(ref = "BadRequest")
    Response simularInvestimento(
            @RequestBody(
                    description = "Dados necessários para realizar a simulação de investimento",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = SimulacaoRequest.class)
                    )
            )
            @Valid @NotNull SimulacaoRequest request);

}
