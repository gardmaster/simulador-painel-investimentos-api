package master.gard.mapper.cliente;

import master.gard.dto.request.cliente.ClienteRequest;
import master.gard.dto.response.cliente.ClienteResponse;
import master.gard.mapper.MapStructConfig;
import master.gard.model.Cliente;
import master.gard.util.DocumentoUtil;
import org.mapstruct.*;

import java.util.List;

@Mapper(config = MapStructConfig.class)
public interface ClienteMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "authUserId", ignore = true)
    @Mapping(target = "dataCriacao", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    Cliente toEntity(ClienteRequest request);

    @Mapping(target = "documento", source = "documento", qualifiedByName = "formatarDocumento")
    @Mapping(target = "perfilRisco", source = "perfilRisco", qualifiedByName = "enumToString")
    ClienteResponse toResponse(Cliente cliente);

    List<ClienteResponse> toResponseList(List<Cliente> clientes);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "authUserId", ignore = true)
    @Mapping(target = "dataCriacao", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    void updateEntityFromRequest(ClienteRequest request, @MappingTarget Cliente cliente);

    @Named("enumToString")
    default String enumToString(Enum<?> value) {
        return value == null ? null : value.name();
    }

    @Named("formatarDocumento")
    default String formatarDocumento(String documento) {
        return documento == null ? null : DocumentoUtil.formatarCpfCnpj(documento);
    }
}
