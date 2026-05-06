package master.gard.repository;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import master.gard.dto.request.ClienteFiltroRequest;
import master.gard.model.Cliente;
import master.gard.model.enums.SortDirection;
import master.gard.model.enums.sort.ClienteSortBy;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@ApplicationScoped
public class ClienteRepository implements PanacheRepository<Cliente> {

    private static final String DOCUMENTO = "documento";
    private static final String EMAIL = "email";

    public boolean isDocumentoExistente(String documento) {
        return count(DOCUMENTO, documento) > 0;
    }

    public boolean isDocumentoCadastradoParaOutroCliente(String documento, Long id) {
        return count("documento = ?1 and id <> ?2", documento, id) > 0;
    }

    public boolean isEmailExistente(String email) {
        return count(EMAIL, email) > 0;
    }

    public boolean isEmailCadastradoParaOutroCliente(String email, Long id) {
        return count("email = ?1 and id <> ?2", email, id) > 0;
    }

    public Optional<Cliente> findByAuthUserIdOptional(String authUserId) {
        return find("authUserId", authUserId).firstResultOptional();
    }

    public PanacheQuery<Cliente> buscarFiltrado(ClienteFiltroRequest filtro) {

        StringBuilder query = new StringBuilder("1 = 1");
        Map<String, Object> params = new HashMap<>();

        if (filtro.getNome() != null && !filtro.getNome().isBlank()) {
            query.append(" and lower(nome) like lower(:nome)");
            params.put("nome", "%" + filtro.getNome().trim() + "%");
        }

        if (filtro.getDocumento() != null && !filtro.getDocumento().isBlank()) {
            query.append(" and documento = :documento");
            params.put(DOCUMENTO, filtro.getDocumento().trim());
        }

        if (filtro.getEmail() != null && !filtro.getEmail().isBlank()) {
            query.append(" and lower(email) like lower(:email)");
            params.put(EMAIL, "%" + filtro.getEmail().trim() + "%");
        }

        if (filtro.getPerfilRisco() != null) {
            query.append(" and perfilRisco = :perfilRisco");
            params.put("perfilRisco", filtro.getPerfilRisco());
        }

        String campoOrdenacao = resolverCampoOrdenacao(filtro.getSortBy());
        String direcaoOrdenacao = resolverDirecao(filtro.getSortDirection());

        query.append(" order by ").append(campoOrdenacao).append(" ").append(direcaoOrdenacao);

        return find(query.toString(), params);
    }

    private String resolverCampoOrdenacao(ClienteSortBy sortBy) {
        if (sortBy == null) {
            return "nome";
        }
        return switch (sortBy) {
            case NOME -> "nome";
            case DOCUMENTO -> DOCUMENTO;
            case EMAIL -> EMAIL;
            case PERFIL_RISCO -> "perfilRisco";
        };
    }

    private String resolverDirecao(SortDirection sortDirection) {
        if (sortDirection == null) {
            return "asc";
        }

        return sortDirection.getValue();
    }
}
