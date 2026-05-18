package master.gard.mapper.cliente;

import master.gard.config.Messages;
import master.gard.dto.response.cliente.PerfilRiscoResponse;
import master.gard.mapper.MapStructConfig;
import master.gard.model.Cliente;
import master.gard.model.enums.PerfilRisco;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(config = MapStructConfig.class)
public interface PerfilRiscoMapper {

    @Mapping(target = "clienteId", source = "id")
    @Mapping(target = "perfil", source = "perfilRisco")
    @Mapping(target = "pontuacao", source = "pontuacaoRisco")
    @Mapping(target = "descricao", source = "perfilRisco", qualifiedByName = "perfilToDescricao")
    PerfilRiscoResponse toResponse(Cliente cliente, @Context Messages msg);

    @Named("perfilToDescricao")
    default String perfilToDescricao(PerfilRisco perfil, @Context Messages msg) {
        if (perfil == null) {
            return msg.get("default.perfilrisco.descricao.nao-definido");
        }
        return msg.get(perfil.getDescricaoKey());
    }
}