package master.gard.service;

import jakarta.enterprise.context.ApplicationScoped;
import master.gard.model.Cliente;
import master.gard.model.Investimento;
import master.gard.model.Simulacao;
import master.gard.model.enums.PerfilRisco;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.List;

@ApplicationScoped
public class PerfilRiscoRecalculoService {

    private static final MathContext MC = new MathContext(16, RoundingMode.HALF_EVEN);
    private static final int SCALE_PONTUACAO = 2;

    private static final BigDecimal PESO_SIMULACAO = new BigDecimal("0.50");

    // Optei por dar mais peso aos investimentos, pois é mais importante no meu ponto de vista
    private static final BigDecimal PESO_INVESTIMENTO = new BigDecimal("2.00");

    // O valor tem um peso, mas algo não tão grande quanto o risco do produto
    private static final BigDecimal PESO_VALOR = new BigDecimal("0.30");

    // Este Valor de Referência foi sugerido pelo copilot como uma forma de evitar distorção da pontuação com valores altos
    // Acredito que faz sentido, especialmente em simulações onde não há de fato um investimento real
    // Talvez no futuro esses 100K possam ser parametrizados com alguma espécie de média de valor investido no produto...
    private static final BigDecimal VALOR_REFERENCIA = new BigDecimal("100000.00");

    public ResultadoRecalculoPerfil recalcularPerfil(Cliente cliente, List<Simulacao> simulacoesCliente,
                                                     List<Investimento> investimentosCliente) {

        BigDecimal somaContribuicao = BigDecimal.ZERO;
        BigDecimal somaPesos = BigDecimal.ZERO;

        for (Simulacao s : simulacoesCliente) {
            BigDecimal scoreEvento = calcularScoreEvento(s.getProduto().getProdutoRisco().getPontuacao(), s.getValorInvestido());
            somaContribuicao = somaContribuicao.add(scoreEvento.multiply(PESO_SIMULACAO, MC), MC);
            somaPesos = somaPesos.add(PESO_SIMULACAO, MC);
        }

        for (Investimento i : investimentosCliente) {
            BigDecimal scoreEvento = calcularScoreEvento(i.getProduto().getProdutoRisco().getPontuacao(), i.getValor());
            somaContribuicao = somaContribuicao.add(scoreEvento.multiply(PESO_INVESTIMENTO, MC), MC);
            somaPesos = somaPesos.add(PESO_INVESTIMENTO, MC);
        }

        // Sem historico: mantem a pontuacao atual do cliente (ou pontuacaoPadrao do perfil, se preferir)
        if (somaPesos.compareTo(BigDecimal.ZERO) == 0) {
            BigDecimal pontuacaoAtual = cliente.getPontuacaoRisco().setScale(SCALE_PONTUACAO, RoundingMode.HALF_EVEN);

            PerfilRisco perfil = resolverPerfilPorPontuacao(pontuacaoAtual);
            return new ResultadoRecalculoPerfil(pontuacaoAtual, perfil);
        }

        BigDecimal novaPontuacao = somaContribuicao.divide(somaPesos, SCALE_PONTUACAO, RoundingMode.HALF_EVEN);

        // clamp defensivo 1..100
        if (novaPontuacao.compareTo(new BigDecimal("1.00")) < 0) {
            novaPontuacao = new BigDecimal("1.00");
        } else if (novaPontuacao.compareTo(new BigDecimal("100.00")) > 0) {
            novaPontuacao = new BigDecimal("100.00");
        }

        PerfilRisco novoPerfil = resolverPerfilPorPontuacao(novaPontuacao);
        return new ResultadoRecalculoPerfil(novaPontuacao, novoPerfil);
    }

    /**
     * Score do evento = (1 - pesoValor) * riscoProduto + pesoValor * fatorValor     * - riscoProduto: pontuacao do enum ProdutoRisco (10, 25, 50, 76, 100)     * - fatorValor: transforma valor financeiro para escala 1..100
     */
    private BigDecimal calcularScoreEvento(int pontuacaoRiscoProduto, BigDecimal valorEvento) {
        BigDecimal riscoProduto = BigDecimal.valueOf(pontuacaoRiscoProduto);

        BigDecimal fatorValor = normalizarValorParaEscala100(valorEvento);

        BigDecimal componenteRisco = BigDecimal.ONE.subtract(PESO_VALOR, MC).multiply(riscoProduto, MC);
        BigDecimal componenteValor = PESO_VALOR.multiply(fatorValor, MC);

        return componenteRisco.add(componenteValor, MC);
    }

    /**
     * Normalizacao simples e estavel:     * fator = min(valor / VALOR_REFERENCIA, 1) * 100     * Ex:     * 10.000 -> 10     * 50.000 -> 50     * 100.000+ -> 100
     */
    private BigDecimal normalizarValorParaEscala100(BigDecimal valor) {
        if (valor == null || valor.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ONE; // piso minimo
        }

        BigDecimal ratio = valor.divide(VALOR_REFERENCIA, 8, RoundingMode.HALF_EVEN);
        if (ratio.compareTo(BigDecimal.ONE) > 0) {
            ratio = BigDecimal.ONE;
        }

        BigDecimal escala100 = ratio.multiply(new BigDecimal("100.00"), MC);
        if (escala100.compareTo(BigDecimal.ONE) < 0) {
            return BigDecimal.ONE;
        }
        return escala100.setScale(2, RoundingMode.HALF_EVEN);
    }

    private PerfilRisco resolverPerfilPorPontuacao(BigDecimal pontuacao) {
        for (PerfilRisco perfil : PerfilRisco.values()) {
            if (perfil.aceita(pontuacao)) {
                return perfil;
            }
        }

        // fallback defensivo (na pratica nao deve cair aqui por causa do clamp)
        if (pontuacao.compareTo(new BigDecimal("25.00")) <= 0) {
            return PerfilRisco.CONSERVADOR;
        } else if (pontuacao.compareTo(new BigDecimal("75.00")) <= 0) {
            return PerfilRisco.MODERADO;
        }
        return PerfilRisco.AGRESSIVO;
    }

    public record ResultadoRecalculoPerfil(
            BigDecimal novaPontuacaoRisco, PerfilRisco novoPerfilRisco
    ) {}

}
