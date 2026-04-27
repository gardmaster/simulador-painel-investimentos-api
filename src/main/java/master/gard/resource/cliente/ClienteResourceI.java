package master.gard.resource.cliente;

import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import master.gard.dto.exception.ProblemDetails;
import master.gard.dto.request.ClienteRequest;
import master.gard.dto.response.ClienteResponse;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.ExampleObject;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@Path("api/v1/clientes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Clientes", description = "Operações de gerenciamento de clientes")
public interface ClienteResourceI {

    @GET
    @RolesAllowed("admin")
    @Operation(
            summary = "Listar todos os clientes",
            description = "Retorna uma lista com todos os clientes cadastrados no sistema."
    )
    @APIResponse(
            responseCode = "200",
            description = "Lista de clientes retornada com sucesso",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = ClienteResponse.class, type = SchemaType.ARRAY)
            )
    )
    Response listarClientes();

    @GET
    @RolesAllowed("admin")
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
    @RolesAllowed({"admin", "user"})
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
            responseCode = "400",
            description = "Requisição inválida - erro de validação",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = ProblemDetails.class),
                    examples = @ExampleObject(
                            name = "CamposInvalidos",
                            value = """
                                    {
                                      "title": "Campos inválidos",
                                      "status": 400,
                                      "detail": "Um ou mais campos estão inválidos. Verifique as violações para mais detalhes.",
                                      "instance": "http://localhost:8080/api/v1/clientes",
                                      "violations": {
                                        "email": ["O campo 'email' deve ser um endereço válido."],
                                        "nome": ["O campo 'nome' é obrigatório."]
                                      }
                                    }
                                    """
                    )
            )
    )
    @APIResponse(
            responseCode = "409",
            description = "Conflito - e-mail ou documento já cadastrado",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = ProblemDetails.class),
                    examples = @ExampleObject(
                            name = "RecursoDuplicado",
                            value = """
                                    {
                                      "title": "Conflito de dados",
                                      "status": 409,
                                      "detail": "Já existe cliente com este e-mail ou documento.",
                                      "instance": "http://localhost:8080/api/v1/clientes"
                                    }
                                    """
                    )
            )
    )
    Response cadastrarCliente(
            @RequestBody(description = "Dados do cliente a ser cadastrado",
                    required = true,
                    content = @Content(schema = @Schema(implementation = ClienteRequest.class)))
            @NotNull @Valid ClienteRequest request);

    @PUT
    @RolesAllowed("admin")
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
    @APIResponse(
            responseCode = "409",
            description = "Conflito - Documento ou e-mail já pertence a outro cliente",
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

    @GET
    @Path("/me")
    @RolesAllowed({"admin", "user"})
    @Operation(
            summary = "Obter dados do cliente autenticado",
            description = "Retorna os detalhes do cliente atualmente autenticado com base no token de autenticação."
    )
    @APIResponse(
            responseCode = "200",
            description = "Dados do cliente autenticado retornados com sucesso",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = ClienteResponse.class)
            )
    )
    @APIResponse(
            responseCode = "401",
            description = "Não autorizado - token de autenticação ausente ou inválido",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = ProblemDetails.class)
            )
    )
    Response obterClienteAutenticado();

    @PUT
    @Path("/me")
    @RolesAllowed({"admin", "user"})
    @Operation(
            summary = "Atualizar dados do cliente autenticado",
            description = "Permite que o cliente atualmente autenticado atualize seus próprios dados com base no token de autenticação."
    )
    @APIResponse(
            responseCode = "200",
            description = "Dados do cliente autenticado atualizados com sucesso",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = ClienteResponse.class)
            )
    )
    @APIResponse(
            responseCode = "400",
            description = "Requisição inválida - erro de validação",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = ProblemDetails.class),
                    examples = @ExampleObject(
                            name = "CamposInvalidos",
                            value = """
                                    {
                                      "title": "Conflito de dados",
                                      "status": 409,
                                      "detail": "Já existe cliente com este e-mail ou documento.",
                                      "instance": "http://localhost:8080/api/v1/clientes"
                                    }
                                    """
                    )
            )
    )
    @APIResponse(
            responseCode = "401",
            description = "Não autorizado - token de autenticação ausente ou inválido",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = ProblemDetails.class)
            )
    )
    Response atualizarClienteAutenticado(
            @RequestBody(
                    description = "Dados atualizados do cliente autenticado",
                    content = @Content(schema = @Schema(implementation = ClienteRequest.class)))

            @Valid @NotNull ClienteRequest request
    );

}
