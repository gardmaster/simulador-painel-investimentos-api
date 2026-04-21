package master.gard.dto.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@Builder
@Schema(description = "Detalhes do problema ocorrido durante o processamento da requisição")
public class ProblemDetails {

    // TODO: Criar um resource para retornar os detalhes do erro

    @Schema(description = "URI que identifica o tipo do problema", examples = "https://example.com/probs/invalid-input")
    private String type;

    @Schema(description = "Título curto e legível do problema", examples = "Campos inválidos")
    private String title;

    @Schema(description = "Código de status HTTP associado ao problema", examples = "400")
    private int status;

    @Schema(description = "Descrição detalhada do problema", examples = "Um ou mais campos estão inválidos. Verifique as violações para mais detalhes.")
    private String detail;

    @Schema(description = "URI da requisição que causou o problema", examples = "http://localhost:8080/api/v1/clientes")
    private String instance;

    @Schema(description = "Mensagem de validação para cada campo inválido",
            examples =
            """
            {
                "nome": ["O campo 'nome' é obrigatório."],
                "email": ["O campo 'email' deve ser um endereço de e-mail válido."],
                "documento": ["O campo 'documento' deve ser um CPF ou CNPJ válido."]
            }
            """)
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Map<String, List<String>> violations;
}
