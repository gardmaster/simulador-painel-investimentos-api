package master.gard.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import master.gard.model.Produto;

@ApplicationScoped
public class ProdutoRepository implements PanacheRepository<Produto> {
}
