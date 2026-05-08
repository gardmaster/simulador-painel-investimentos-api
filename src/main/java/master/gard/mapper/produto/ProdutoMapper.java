package master.gard.mapper.produto;

import master.gard.dto.request.ProdutoRequest;
import master.gard.dto.response.ProdutoResponse;
import master.gard.mapper.MapStructConfig;
import master.gard.model.Produto;
import org.mapstruct.*;

import java.util.List;

@Mapper(config = MapStructConfig.class)
public interface ProdutoMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dataCriacao", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    Produto toEntity(ProdutoRequest request);

    @Mapping(target = "tipoProduto", source = "tipoProduto", qualifiedByName = "enumToString")
    @Mapping(target = "produtoRisco", source = "produtoRisco", qualifiedByName = "enumToString")
    ProdutoResponse toResponse(Produto produto);

    List<ProdutoResponse> toResponseList(List<Produto> produtos);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dataCriacao", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    void updateEntityFromRequest(ProdutoRequest request, @MappingTarget Produto produto);

    @Named("enumToString")
    default String enumToString(Enum<?> value) {
        return value == null ? null : value.name();
    }
}
