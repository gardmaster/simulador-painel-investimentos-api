package master.gard.dto.response.simulacao;

import master.gard.dto.response.PageInfoResponse;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.util.List;

@Schema(name = "SimulacaoPageResponse", description = "Resposta paginada para listagem de simulações, " +
        "contendo os dados das simulações e informações de paginação.")
public record SimulacaoPageResponse(

        @Schema(description = "Lista de simulações retornadas na página atual.")
        List<SimulacaoResponse> simulacoes,

        @Schema(description = "Metadados de paginação da consulta")
        PageInfoResponse pageInfo

) {
}
