package master.gard.exception;

import lombok.Getter;

@Getter
public class RentabilidadeProdutoInvertidaException extends RuntimeException {

    private final Double rentabilidadeMin;
    private final Double rentabilidadeMax;

    public RentabilidadeProdutoInvertidaException(Double rentabilidadeMin, Double rentabilidadeMax) {
        this.rentabilidadeMin = rentabilidadeMin;
        this.rentabilidadeMax = rentabilidadeMax;
    }
}
