package master.gard.model.enums;

import lombok.Getter;

@Getter
public enum ProdutoRisco {

    BAIXISSIMO("Baixíssimo risco", 10),
    BAIXO("Baixo risco", 25),
    MEDIO("Médio risco", 50),
    ALTO("Alto risco", 76),
    ALTISSIMO("Altíssimo risco", 100);

    private final String descricao;
    private final int pontuacao;

    ProdutoRisco(String descricao, int pontuacao) {
        this.descricao = descricao;
        this.pontuacao = pontuacao;
    }

}
