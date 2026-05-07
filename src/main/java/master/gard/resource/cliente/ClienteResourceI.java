package master.gard.resource.cliente;

import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import master.gard.dto.request.ClienteFiltroRequest;
import master.gard.dto.request.ClienteRequest;
import master.gard.dto.response.ClientePageResponse;
import master.gard.dto.response.ClienteResponse;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@Path("api/v1/clientes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Clientes")
public interface ClienteResourceI {

    @GET
    @RolesAllowed("admin")
    @Operation(
            summary = "Listar clientes",
            description = "Retorna uma resposta paginada com os clientes cadastrados, " +
                    "permitindo filtros por nome, documento, e-mail, perfil de risco e ordenação."
    )
    @APIResponse(
            responseCode = "200",
            description = "Lista de clientes retornada com sucesso",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = ClientePageResponse.class)
            )
    )
    Response listarClientes(@BeanParam ClienteFiltroRequest filtro);

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
    @APIResponse(ref = "NotFoundCliente")
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
    @APIResponse(ref = "BadRequest")
    @APIResponse(ref = "ConflictClienteDuplicado")
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
    @APIResponse(ref = "BadRequest")
    @APIResponse(ref = "NotFoundCliente")
    @APIResponse(ref = "ConflictClienteDuplicado")
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
    @APIResponse(ref = "BadRequest")
    @APIResponse(ref = "ConflictClienteDuplicado")
    Response atualizarClienteAutenticado(
            @RequestBody(
                    description = "Dados atualizados do cliente autenticado",
                    content = @Content(schema = @Schema(implementation = ClienteRequest.class)))

            @Valid @NotNull ClienteRequest request
    );

}
