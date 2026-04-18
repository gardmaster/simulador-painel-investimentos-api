package master.gard.dto.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

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

}
