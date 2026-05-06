package master.gard.dto.response;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(name = "PageResponse", description = "Informações de paginação para respostas paginadas")
public record PageResponse(

        @Schema(description = "Número da página atual", examples = "1")
        int page,

        @Schema(description = "Quantidade de registros por página", examples = "5")
        int pageSize,

        @Schema(description = "Total de registros encontrados", examples = "20")
        long totalElements,

        @Schema(description = "Total de páginas disponíveis", examples = "4")
        int totalPages

) {
}
