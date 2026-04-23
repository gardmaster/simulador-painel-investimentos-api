package master.gard.exception.mapper;

import io.quarkus.logging.Log;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import master.gard.config.MessageKeys;
import master.gard.config.Messages;
import master.gard.dto.exception.ProblemDetails;
import org.jboss.logging.Logger;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Provider
public class ConstraintViolationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {

    private static final Logger LOG = Logger.getLogger(ConstraintViolationExceptionMapper.class);

    @Context
    UriInfo uriInfo;

    private final Messages msg;

    public ConstraintViolationExceptionMapper(Messages msg) {
        this.msg = msg;
    }

    @Override
    public Response toResponse(jakarta.validation.ConstraintViolationException exception) {
        LOG.warnf("Validação falhou: %d violações encontradas", exception.getConstraintViolations().size());

        Map<String, List<String>> violations = exception.getConstraintViolations().stream()
                .collect(Collectors.groupingBy(
                        cv -> {
                            String path = cv.getPropertyPath().toString();
                            String[] parts = path.split("\\.");
                            return parts[parts.length - 1];
                        },
                        LinkedHashMap::new,
                        Collectors.mapping(ConstraintViolation::getMessage, Collectors.toList())
                ));

        ProblemDetails problemDetails = ProblemDetails.builder()
                .status(Response.Status.BAD_REQUEST.getStatusCode())
                .title(msg.get(MessageKeys.CAMPOS_INVALIDOS_TITLE))
                .detail(msg.get(MessageKeys.CAMPOS_INVALIDOS_DETAIL))
                .instance(uriInfo != null ? uriInfo.getRequestUri().toString() : "")
                .violations(violations)
                .build();

        return Response.status(Response.Status.BAD_REQUEST)
                .entity(problemDetails)
                .build();
    }
}
