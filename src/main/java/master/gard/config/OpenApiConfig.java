package master.gard.config;

import jakarta.ws.rs.core.Application;
import jakarta.ws.rs.core.MediaType;
import master.gard.dto.exception.ProblemDetails;
import org.eclipse.microprofile.openapi.annotations.Components;
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Contact;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.ExampleObject;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.servers.Server;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@OpenAPIDefinition(
        info = @Info(
                title = "Painel de Investimentos API",
                version = "1.0",
                description = """
                        API RESTful para simulação de investimentos baseado em perfil dinâmico do investidor.
                        
                        ## Funcionalidades
                        - Cadastro e gestão de clientes
                        - Simulação de investimentos
                        - Recomendação de investimentos baseados em perfil de risco
                        - Perfil de risco dinâmico com base em comportamento de investimentos e/ou simulações
                        - Telemetria de dados
                        """,
                contact = @Contact(
                        name = "Giovanni Duarte",
                        email = "garduarte@teste.com"
                )
        ),
        servers = {
                @Server(url = "http://localhost:8080", description = "Ambiente de Desenvolvimento")
        },
        tags = {
                @Tag(name = "Clientes", description = "Operações de gerenciamento de clientes"),
                @Tag(name = "Produtos", description = "Operações de gerenciamento de produtos financeiros")
        },
        components = @Components(
                responses = {
                        @APIResponse(
                                name = "BadRequest",
                                responseCode = "400",
                                description = "Requisição inválida",
                                content = @Content(
                                        mediaType = MediaType.APPLICATION_JSON,
                                        schema = @Schema(implementation = ProblemDetails.class),
                                        examples = @ExampleObject(
                                                name = "CamposInvalidos",
                                                value = """
                                                        {
                                                          "title": "Requisição inválida",
                                                          "status": 400,
                                                          "detail": "Um ou mais campos estão inválidos",
                                                          "instance": "http://localhost:8080/api/v1/recurso",
                                                          "violations": {
                                                            "email": ["O campo 'email' deve ser um endereço válido."],
                                                            "nome": ["O campo 'nome' é obrigatório."]
                                                          }
                                                        }
                                                        """
                                        )
                                )
                        ),
                        @APIResponse(
                                name = "NotFoundCliente",
                                responseCode = "404",
                                description = "Cliente não encontrado",
                                content = @Content(
                                        mediaType = MediaType.APPLICATION_JSON,
                                        schema = @Schema(implementation = ProblemDetails.class),
                                        examples = @ExampleObject(
                                                name = "ClienteNaoEncontrado",
                                                value = """
                                                        {
                                                          "title": "Cliente não encontrado",
                                                          "status": 404,
                                                          "detail": "Não foi encontrado cliente com o identificador informado.",
                                                          "instance": "http://localhost:8080/api/v1/clientes/1"
                                                        }
                                                        """
                                        )
                                )
                        ),
                        @APIResponse(
                                name = "NotFoundProduto",
                                responseCode = "404",
                                description = "Produto não encontrado",
                                content = @Content(
                                        mediaType = MediaType.APPLICATION_JSON,
                                        schema = @Schema(implementation = ProblemDetails.class),
                                        examples = @ExampleObject(
                                                name = "ProdutoNaoEncontrado",
                                                value = """
                                                        {
                                                          "title": "Produto não encontrado",
                                                          "status": 404,
                                                          "detail": "Não foi encontrado produto com o identificador informado.",
                                                          "instance": "http://localhost:8080/api/v1/produtos/1"
                                                        }
                                                        """
                                        )
                                )
                        ),
                        @APIResponse(
                                name = "ConflictClienteDuplicado",
                                responseCode = "409",
                                description = "Conflito - e-mail ou documento já cadastrado",
                                content = @Content(
                                        mediaType = MediaType.APPLICATION_JSON,
                                        schema = @Schema(implementation = ProblemDetails.class),
                                        examples = @ExampleObject(
                                                name = "ClienteDuplicado",
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
                        ),
                        @APIResponse(
                                name = "ConflictProdutoDuplicado",
                                responseCode = "409",
                                description = "Conflito - já existe produto com o mesmo nome",
                                content = @Content(
                                        mediaType = MediaType.APPLICATION_JSON,
                                        schema = @Schema(implementation = ProblemDetails.class),
                                        examples = @ExampleObject(
                                                name = "ProdutoDuplicado",
                                                value = """
                                                        {
                                                          "title": "Conflito de dados",
                                                          "status": 409,
                                                          "detail": "Já existe produto com o mesmo nome.",
                                                          "instance": "http://localhost:8080/api/v1/produtos"
                                                        }
                                                        """
                                        )
                                )
                        )
                }
        )
)
public class OpenApiConfig extends Application {
}
