package master.gard.exception.mapper;

import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import master.gard.config.MessageKeys;
import master.gard.config.Messages;
import master.gard.dto.exception.ProblemDetails;
import master.gard.exception.ProdutoNaoEncontradoException;
import org.jboss.logging.Logger;

@Provider
public class ProdutoNaoEncontradoExceptionMapper implements ExceptionMapper<ProdutoNaoEncontradoException> {

    private static final Logger LOG = Logger.getLogger(ProdutoNaoEncontradoExceptionMapper.class);

    @Context
    UriInfo uriInfo;

    private final Messages msg;

    public ProdutoNaoEncontradoExceptionMapper(Messages msg) {
        this.msg = msg;
    }

    @Override
    public Response toResponse(ProdutoNaoEncontradoException exception) {

        LOG.warnf("Produto com ID %d não encontrado", exception.getId());

        ProblemDetails problemDetails = ProblemDetails.builder()
                .status(Response.Status.NOT_FOUND.getStatusCode())
                .title(msg.get(MessageKeys.PRODUTO_NAO_ENCONTRADO_TITLE))
                .detail(msg.format(MessageKeys.PRODUTO_NAO_ENCONTRADO_DETAIL, exception.getId()))
                .instance(uriInfo != null ? uriInfo.getRequestUri().toString() : "")
                .build();

        return Response.status(Response.Status.NOT_FOUND)
                .entity(problemDetails)
                .build();
    }
}
