package master.gard.model.enums;

import lombok.Getter;

@Getter
public enum TipoProduto {

    CDB("Certificado de Depósito Bancário"),
    LCI("Letra de Crédito Imobiliário"),
    LCA("Letra de Crédito do Agronegócio"),
    TESOURO_DIRETO_SELIC("Tesouro Direto Selic"),
    FIA("Fundo de Investimento em Ações"),
    FII("Fundo de Investimento Imobiliário"),
    CRI("Certificado de Recebíveis Imobiliários"),
    CRA("Certificado de Recebíveis do Agronegócio"),
    DEBENTURE("Debênture");

    private final String descricao;

    TipoProduto(String descricao) {
        this.descricao = descricao;
    }

    public boolean possuiVencimento() {
        return switch (this) {
            case CDB, LCI, LCA, TESOURO_DIRETO_SELIC, CRI, CRA, DEBENTURE -> true;
            case FIA, FII -> false;
        };
    }

}
