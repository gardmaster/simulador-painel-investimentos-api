package master.gard.resource;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import master.gard.dto.exception.ProblemDetails;
import master.gard.dto.request.ClienteRequest;
import master.gard.dto.response.ClienteResponse;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@Path("api/v1/clientes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Clientes", description = "Operações de gerenciamento de clientes")
public interface ClienteResourceI {

    @GET
    @Operation(
            summary = "Listar todos os clientes",
            description = "Retorna uma lista com todos os clientes cadastrados no sistema."
    )
    @APIResponses({
            @APIResponse(
                    responseCode = "200",
                    description = "Lista de clientes retornada com sucesso",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = ClienteResponse.class)
                    )
            )
    })
    Response listarClientes();

    @GET
    @Path("/{id}")
    @Operation(
            summary = "Obter cliente por ID",
            description = "Retorna os detalhes de um cliente específico com base no seu ID."
    )
    @APIResponse(
            responseCode = "200",
            description = "Cliente encontrado e retornado com sucesso",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = ClienteResponse.class)
            )
    )
    @APIResponse(
            responseCode = "404",
            description = "Cliente não encontrado",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = ProblemDetails.class)
            )

    )
    Response obterClientePorId(
            @Parameter(description = "Identificador único do cliente", required = true, example = "1")
            @PathParam("id") Long id
    );

    @POST
    @Operation(
            summary = "Cadastrar novo cliente",
            description = "Permite cadastrar um novo cliente no sistema."
    )
    @APIResponse(
            responseCode = "201",
            description = "Cliente cadastrado com sucesso",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = ClienteResponse.class)
            )
    )
    @APIResponse(
            responseCode = "409",
            description = "Conflito - Cliente já existe",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = ProblemDetails.class)
            )
    )
    @APIResponse(
            responseCode = "400",
            description = "Requisição inválida - Dados do cliente estão incorretos ou incompletos",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = ProblemDetails.class)
            )
    )
    Response cadastrarCliente(
            @RequestBody(description = "Dados do cliente a ser cadastrado",
                    required = true,
                    content = @Content(schema = @Schema(implementation = ClienteRequest.class)))
            @NotNull @Valid ClienteRequest request);

    @PUT
    @Path("/{id}")
    @Operation(
            summary = "Atualizar cliente existente",
            description = "Permite atualizar os dados de um cliente existente com base no seu ID."
    )
    @APIResponse(
            responseCode = "200",
            description = "Cliente atualizado com sucesso",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = ClienteResponse.class)
            )
    )
    @APIResponse(
            responseCode = "404",
            description = "Cliente não encontrado",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = ProblemDetails.class)
            )
    )
    Response atualizarCadastroCliente(
            @Parameter(description = "Identificador único do cliente a ser atualizado",
                    required = true,
                    example = "1")
            @PathParam("id") Long id,

            @RequestBody(description = "Dados atualizados do cliente",
                    content = @Content(schema = @Schema(implementation = ClienteRequest.class)))
            @Valid @NotNull ClienteRequest request
    );
}
