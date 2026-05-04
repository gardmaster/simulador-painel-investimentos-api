package master.gard.exception.mapper;

import jakarta.inject.Inject;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import master.gard.config.MessageKeys;
import master.gard.config.Messages;
import master.gard.dto.exception.ProblemDetails;
import master.gard.exception.RentabilidadeProdutoInvertidaException;
import org.jboss.logging.Logger;

@Provider
public class RentabilidadeProdutoInvertidaExceptionMapper implements ExceptionMapper<RentabilidadeProdutoInvertidaException> {

    private static final Logger LOG = Logger.getLogger(RentabilidadeProdutoInvertidaExceptionMapper.class);

    @Context
    private UriInfo uriInfo;

    private final Messages msg;

    @Inject
    public RentabilidadeProdutoInvertidaExceptionMapper(Messages msg) {
        this.msg = msg;
    }

    @Override
    public Response toResponse(RentabilidadeProdutoInvertidaException e) {

        LOG.warnf("Rentabilidade mínima maior que máxima: min=%s, max=%s", e.getRentabilidadeMin(), e.getRentabilidadeMax());

        ProblemDetails problemDetails = ProblemDetails.builder()
                .status(Response.Status.BAD_REQUEST.getStatusCode())
                .title(msg.get(MessageKeys.RENTABILIDADE_PRODUTO_INVERTIDA_TITLE))
                .detail(msg.get(MessageKeys.RENTABILIDADE_PRODUTO_INVERTIDA_DETAIL))
                .instance(uriInfo != null ? uriInfo.getRequestUri().toString() : "")
                .build();

        return Response.status(Response.Status.BAD_REQUEST)
                .entity(problemDetails)
                .build();
    }
}
