package master.gard.exception.mapper;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import master.gard.config.MessageKeys;
import master.gard.config.Messages;
import master.gard.exception.ClaimSubInexistenteException;


@Provider
public class ClaimSubInexistenteExceptionMapper extends BaseExceptionMapper
        implements ExceptionMapper<ClaimSubInexistenteException> {

    public ClaimSubInexistenteExceptionMapper(Messages msg) {
        super(msg);
    }

    @Override
    public Response toResponse(ClaimSubInexistenteException exception) {

        log.warnf("""
                Claim 'sub' inexistente no token JWT.
                Preferred Username: %s
                Nome: %s
                Email: %s
                """, exception.getPreferredUsername(), exception.getNome(), exception.getEmail());

        return buildResponse(
                Response.Status.INTERNAL_SERVER_ERROR,
                msg.get(MessageKeys.CLAIM_SUB_INEXISTENTE_TITLE),
                msg.format(MessageKeys.CLAIM_SUB_INEXISTENTE_DETAIL, exception.getPreferredUsername())
        );

    }
}
