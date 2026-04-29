package master.gard.exception.mapper;

import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import master.gard.config.MessageKeys;
import master.gard.config.Messages;
import master.gard.dto.exception.ProblemDetails;
import master.gard.exception.DocumentoExistenteException;
import master.gard.exception.ProdutoExistenteException;
import org.jboss.logging.Logger;

@Provider
public class ProdutoExistenteExceptionMapper implements ExceptionMapper<ProdutoExistenteException> {

    private static final Logger LOG = Logger.getLogger(ProdutoExistenteExceptionMapper.class);

    @Context
    UriInfo uriInfo;

    private final Messages msg;

    public ProdutoExistenteExceptionMapper(Messages msg) {
        this.msg = msg;
    }

    @Override
    public Response toResponse(ProdutoExistenteException exception) {
        LOG.warnf("Produto de mesmo nome encontrado: %s", exception.getNome());

        ProblemDetails problemDetails = ProblemDetails.builder()
                .status(Response.Status.CONFLICT.getStatusCode())
                .title(msg.get(MessageKeys.PRODUTO_NOME_DUPLICADO_TITLE))
                .detail(msg.get(MessageKeys.PRODUTO_NOME_DUPLICADO_DETAIL))
                .instance(uriInfo != null ? uriInfo.getRequestUri().toString() : "")
                .build();

        return Response.status(Response.Status.CONFLICT)
                .entity(problemDetails)
                .build();
    }
}
