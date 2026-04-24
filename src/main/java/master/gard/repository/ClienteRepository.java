package master.gard.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import master.gard.model.Cliente;

import java.util.Optional;

@ApplicationScoped
public class ClienteRepository implements PanacheRepository<Cliente> {

    public boolean isDocumentoExistente(String documento) {
        return count("documento", documento) > 0;
    }

    public boolean isDocumentoCadastradoParaOutroCliente(String documento, Long id) {
        return count("documento = ?1 and id <> ?2", documento, id) > 0;
    }

    public boolean isEmailExistente(String email) {
        return count("email", email) > 0;
    }

    public boolean isEmailCadastradoParaOutroCliente(String email, Long id) {
        return count("email = ?1 and id <> ?2", email, id) > 0;
    }

    public Optional<Cliente> findByAuthUserIdOptional(String authUserId) {
        return find("authUserId", authUserId).firstResultOptional();
    }
}
