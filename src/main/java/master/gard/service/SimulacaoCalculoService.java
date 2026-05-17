package master.gard.service;

import jakarta.enterprise.context.ApplicationScoped;
import master.gard.model.Cliente;
import master.gard.model.Produto;
import master.gard.model.Simulacao;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.Instant;

@ApplicationScoped
public class SimulacaoCalculoService {

    private static final int SCALE_MOEDA = 2;
    private static final int SCALE_RENTABILIDADE = 6;
    private static final MathContext MC = new MathContext(16, RoundingMode.HALF_EVEN);

    public Simulacao calcularSimulacao(Cliente cliente, Produto produto, BigDecimal valorInvestido, Integer prazoMeses) {

        BigDecimal rentabilidadeMensal = produto.getRentabilidadeMensal();

        BigDecimal valorFinal = calcularValorFinal(valorInvestido, rentabilidadeMensal, prazoMeses);
        BigDecimal rentabilidadeEfetiva = calcularRentabilidadeEfetiva(valorInvestido, valorFinal);

        Simulacao simulacao = new Simulacao();
        simulacao.setCliente(cliente);
        simulacao.setProduto(produto);
        simulacao.setValorInvestido(valorInvestido.setScale(SCALE_MOEDA, RoundingMode.HALF_EVEN));
        simulacao.setValorFinal(valorFinal);
        simulacao.setRentabilidadeEfetiva(rentabilidadeEfetiva);
        simulacao.setPrazoMeses(prazoMeses);
        simulacao.setDataSimulacao(Instant.now());

        return simulacao;
    }

    private BigDecimal calcularValorFinal(BigDecimal valorInvestido, BigDecimal rentabilidadeMensal, Integer prazoMeses) {
        BigDecimal fator = BigDecimal.ONE.add(rentabilidadeMensal, MC).pow(prazoMeses, MC);
        return valorInvestido.multiply(fator, MC).setScale(SCALE_MOEDA, RoundingMode.HALF_EVEN);
    }

    private BigDecimal calcularRentabilidadeEfetiva(BigDecimal valorInvestido, BigDecimal valorFinal) {
        return valorFinal.divide(valorInvestido, SCALE_RENTABILIDADE + 4, RoundingMode.HALF_EVEN)
                .subtract(BigDecimal.ONE, MC)
                .setScale(SCALE_RENTABILIDADE, RoundingMode.HALF_EVEN);
    }

}
