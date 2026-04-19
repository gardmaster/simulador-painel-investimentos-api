package master.gard.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Locale;

public class CpfCnpjValidator implements ConstraintValidator<CpfCnpj, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) {
            return true;
        }

        String documento = value.trim().toUpperCase(Locale.ROOT);

        if (!documento.matches("^[A-Z0-9]+$")) {
            return false;
        }

        if (documento.matches("^\\d{11}$")) {
            return isCpfValido(documento);
        }

        if (documento.matches("^[A-Z0-9]{12}\\d{2}$")) {
            return isCnpjValido(documento);
        }

        return false;
    }

    private boolean isCpfValido(String cpf) {
        if (cpf.length() != 11) {
            return false;
        }

        if (todosCaracteresIguais(cpf)) {
            return false;
        }

        int digito1 = calcularDigitoCpf(cpf.substring(0, 9), 10);
        int digito2 = calcularDigitoCpf(cpf.substring(0, 9) + digito1, 11);

        return cpf.equals(cpf.substring(0, 9) + digito1 + digito2);
    }

    private int calcularDigitoCpf(String base, int pesoInicial) {
        int soma = 0;
        int peso = pesoInicial;

        for (int i = 0; i < base.length(); i++) {
            soma += Character.getNumericValue(base.charAt(i)) * peso--;
        }

        int resto = soma % 11;
        return resto < 2 ? 0 : 11 - resto;
    }

    private boolean isCnpjValido(String cnpj) {
        if (cnpj.length() != 14) {
            return false;
        }

        if (todosCaracteresIguais(cnpj)) {
            return false;
        }

        String base = cnpj.substring(0, 12);
        String dvInformado = cnpj.substring(12);

        int digito1 = calcularDigitoCnpj(base, new int[]{5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2});
        int digito2 = calcularDigitoCnpj(base + digito1, new int[]{6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2});

        return dvInformado.equals("" + digito1 + digito2);
    }

    private int calcularDigitoCnpj(String base, int[] pesos) {
        int soma = 0;

        for (int i = 0; i < base.length(); i++) {
            soma += valorCaracterCnpj(base.charAt(i)) * pesos[i];
        }

        int resto = soma % 11;
        return resto < 2 ? 0 : 11 - resto;
    }

    private int valorCaracterCnpj(char c) {
        return c - 48;
    }

    private boolean todosCaracteresIguais(String valor) {
        char primeiro = valor.charAt(0);
        for (int i = 1; i < valor.length(); i++) {
            if (valor.charAt(i) != primeiro) {
                return false;
            }
        }
        return true;
    }
}
