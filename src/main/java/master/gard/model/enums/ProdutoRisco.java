package master.gard.model.enums;

import lombok.Getter;

@Getter
public enum ProdutoRisco {

    BAIXISSIMO("Baixíssimo risco", 90),
    BAIXO("Baixo risco", 75),
    MEDIO("Médio risco", 50),
    ALTO("Alto risco", 25),
    ALTISSIMO("Altíssimo risco", 10);

    private final String descricao;
    private final int pontuacao;

    ProdutoRisco(String descricao, int pontuacao) {
        this.descricao = descricao;
        this.pontuacao = pontuacao;
    }

}
