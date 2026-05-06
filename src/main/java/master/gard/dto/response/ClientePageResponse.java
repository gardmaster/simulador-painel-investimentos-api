package master.gard.dto.response;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.util.List;

@Schema(name = "ClientePageResponse", description = "Resposta paginada com clientes")
public record ClientePageResponse(

        @Schema(description = "Lista de clientes da página atual")
        List<ClienteResponse> data,

        PageResponse pageInfo

) {
}
