package master.gard.exception;

import lombok.Getter;

@Getter
public class ProdutoExistenteException extends RuntimeException {

    private final String nome;

    public ProdutoExistenteException(String nome) {
        this.nome = nome;
    }

}
