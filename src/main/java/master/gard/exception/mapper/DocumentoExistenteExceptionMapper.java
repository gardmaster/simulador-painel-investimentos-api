package master.gard.exception.mapper;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import master.gard.config.MessageKeys;
import master.gard.config.Messages;
import master.gard.exception.DocumentoExistenteException;

@Provider
public class DocumentoExistenteExceptionMapper extends BaseExceptionMapper
        implements ExceptionMapper<DocumentoExistenteException> {

    public DocumentoExistenteExceptionMapper(Messages msg) {
        super(msg);
    }

    @Override
    public Response toResponse(DocumentoExistenteException exception) {

        log.warnf("Documento duplicado encontrado: %s", exception.getDocumento());

        return buildResponse(
                Response.Status.CONFLICT,
                msg.get(MessageKeys.CLIENTE_DOCUMENTO_DUPLICADO_TITLE),
                msg.format(MessageKeys.CLIENTE_DOCUMENTO_DUPLICADO_DETAIL, exception.getDocumento())
        );

    }
}
