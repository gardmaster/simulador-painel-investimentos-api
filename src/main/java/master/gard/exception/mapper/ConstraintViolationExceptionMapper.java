package master.gard.exception.mapper;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import master.gard.config.MessageKeys;
import master.gard.config.Messages;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Provider
public class ConstraintViolationExceptionMapper extends BaseExceptionMapper
        implements ExceptionMapper<ConstraintViolationException> {

    public ConstraintViolationExceptionMapper(Messages msg) {
        super(msg);
    }

    @Override
    public Response toResponse(jakarta.validation.ConstraintViolationException exception) {

        log.warnf("Validação falhou: %d violações encontradas", exception.getConstraintViolations().size());

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

        return buildResponse(
                Response.Status.BAD_REQUEST,
                msg.get(MessageKeys.CAMPOS_INVALIDOS_TITLE),
                msg.get(MessageKeys.CAMPOS_INVALIDOS_DETAIL),
                violations
        );

    }
}
