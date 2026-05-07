package master.gard.exception.mapper;

import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import master.gard.config.Messages;
import master.gard.dto.exception.ProblemDetails;
import org.jboss.logging.Logger;

import java.util.List;
import java.util.Map;

public abstract class BaseExceptionMapper {

    @Context
    protected UriInfo uriInfo;

    protected final Messages msg;
    protected final Logger log = Logger.getLogger(getClass());

    protected BaseExceptionMapper(Messages msg) {
        this.msg = msg;
    }

    protected Response buildResponse(Response.Status status, String title, String detail) {
        ProblemDetails problemDetails = ProblemDetails.builder()
                .status(status.getStatusCode())
                .title(title)
                .detail(detail)
                .instance(getInstance())
                .build();

        return Response.status(status)
                .entity(problemDetails)
                .build();
    }

    protected Response buildResponse(
            Response.Status status,
            String title,
            String detail,
            Map<String, List<String>> violations
    ) {
        ProblemDetails problemDetails = ProblemDetails.builder()
                .status(status.getStatusCode())
                .title(title)
                .detail(detail)
                .instance(getInstance())
                .violations(violations)
                .build();

        return Response.status(status)
                .entity(problemDetails)
                .build();
    }

    protected String getInstance() {
        return uriInfo != null ? uriInfo.getRequestUri().toString() : "";
    }

}
