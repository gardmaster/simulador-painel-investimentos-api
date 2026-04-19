package master.gard.dto.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class ProblemDetails {

    // TODO: Criar um resource para retornar os detalhes do erro
    private String type;
    private String title;
    private int status;
    private String detail;
    private String instance;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Map<String, List<String>> violations;
}
