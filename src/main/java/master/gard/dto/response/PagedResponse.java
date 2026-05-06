package master.gard.dto.response;

import java.util.List;

public record PagedResponse<T>(
        List<T> data,
        int page,
        int pageSize,
        long totalElements,
        int totalPages
) {
}
