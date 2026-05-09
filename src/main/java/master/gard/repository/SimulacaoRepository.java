package master.gard.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import master.gard.model.Simulacao;

@ApplicationScoped
public class SimulacaoRepository implements PanacheRepository<Simulacao> {
}
