package master.gard.dto.response;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.util.List;

@Schema(name = "ProdutoPageResponse", description = "Resposta paginada com produtos financeiros")
public record ProdutoPageResponse(

        @Schema(description = "Lista de produtos da página atual")
        List<ProdutoResponse> produtos,

        @Schema(description = "Metadados de paginação da consulta")
        PageInfoResponse pageInfo

) {
}
