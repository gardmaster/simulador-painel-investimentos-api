package master.gard.model.enums;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(description = "Perfil de risco do investidor")
public enum PerfilRisco {

    @Schema(description = "Investidor conservador, com baixa tolerância a riscos e foco em segurança do capital")
    CONSERVADOR,

    @Schema(description = "Investidor moderado, com tolerância média a riscos e busca por equilíbrio entre segurança e retorno")
    MODERADO,

    @Schema(description = "Investidor agressivo, com altíssima tolerância a riscos e busca por retornos máximos, mesmo que isso implique em grandes oscilações")
    AGRESSIVO;

}
