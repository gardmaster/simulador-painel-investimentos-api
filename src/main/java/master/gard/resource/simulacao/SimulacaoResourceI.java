package master.gard.resource.simulacao;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import master.gard.dto.request.simulacao.SimulacaoFiltroRequest;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@Path("/simulacoes")
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



}
