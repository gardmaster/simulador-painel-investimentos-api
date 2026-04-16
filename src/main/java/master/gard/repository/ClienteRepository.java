package master.gard.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import master.gard.model.Cliente;

@ApplicationScoped
public class ClienteRepository implements PanacheRepository<Cliente> {

    public boolean isDocumentoExistente(String documento) {
        return find("documento", documento).firstResultOptional().isPresent();
    }

    public boolean isEmailExistente(String email) {
        return find("email", email).firstResultOptional().isPresent();
    }
}
