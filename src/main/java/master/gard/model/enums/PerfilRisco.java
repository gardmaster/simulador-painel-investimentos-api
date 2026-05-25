package master.gard.model.enums;

import lombok.Getter;
import master.gard.config.MessageKeys;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.math.BigDecimal;

@Getter
@Schema(description = "Perfil de risco do investidor")
public enum PerfilRisco {

    @Schema(description = "Investidor conservador, com baixa tolerância a riscos e foco em segurança do capital")
    CONSERVADOR(
            new BigDecimal("12.50"),
            new BigDecimal("1.00"),
            new BigDecimal("25.00"),
            MessageKeys.PERFIL_RISCO_CONSERVADOR
    ),

    @Schema(description = "Investidor moderado, com tolerância média a riscos e busca por equilíbrio entre segurança e retorno")
    MODERADO(new BigDecimal("50.00"),
            new BigDecimal("26.00"),
            new BigDecimal("75.00"),
            MessageKeys.PERFIL_RISCO_MODERADO
    ),

    @Schema(description = "Investidor agressivo, com altíssima tolerância a riscos e busca por retornos máximos, mesmo que isso implique em grandes oscilações")
    AGRESSIVO(new BigDecimal("87.50"),
            new BigDecimal("76.00"),
            new BigDecimal("100.00"),
            MessageKeys.PERFIL_RISCO_AGRESSIVO
    );

    private final BigDecimal pontuacaoPadrao;
    private final BigDecimal faixaMin;
    private final BigDecimal faixaMax;
    private final String messageKeyDescricao;

    PerfilRisco(BigDecimal pontuacaoPadrao, BigDecimal faixaMin, BigDecimal faixaMax, String messageKeyDescricao) {
        this.pontuacaoPadrao = pontuacaoPadrao;
        this.faixaMin = faixaMin;
        this.faixaMax = faixaMax;
        this.messageKeyDescricao = messageKeyDescricao;
    }

    public boolean aceita(BigDecimal pontuacao) {
        return pontuacao != null
                && pontuacao.compareTo(faixaMin) >= 0
                && pontuacao.compareTo(faixaMax) <= 0;
    }

}
