package master.gard.service;

import jakarta.enterprise.context.ApplicationScoped;
import master.gard.repository.SimulacaoRepository;

@ApplicationScoped
public class SimulacaoService {

    private final SimulacaoRepository simulacaoRepository;

    public SimulacaoService(SimulacaoRepository simulacaoRepository) {
        this.simulacaoRepository = simulacaoRepository;
    }

}
