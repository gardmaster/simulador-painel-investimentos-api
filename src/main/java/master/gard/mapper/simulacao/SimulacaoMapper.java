package master.gard.mapper.simulacao;

import master.gard.dto.response.simulacao.SimulacaoResponse;
import master.gard.mapper.MapStructConfig;
import master.gard.model.Simulacao;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.time.Instant;
import java.util.List;

@Mapper(config = MapStructConfig.class)
public interface SimulacaoMapper {

    @Mapping(target = "clienteId", source = "cliente.id")
    @Mapping(target = "nomeProduto", source = "produto.nome")
    @Mapping(target = "dataSimulacao", source = "dataSimulacao", qualifiedByName = "instantToString")
    SimulacaoResponse toResponse(Simulacao simulacao);

    List<SimulacaoResponse> toResponseList(List<Simulacao> simulacoes);

    @Named("instantToString")
    default String instantToString(Instant value) {
        return value == null ? null : value.toString();
    }

}
