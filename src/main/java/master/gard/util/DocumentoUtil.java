package master.gard.util;

public final class DocumentoUtil {

    private DocumentoUtil() {
    }

    public static String formatarCpfCnpj(String documento) {
        if (documento != null && documento.length() == 11) {
            return documento.replaceAll("(\\d{3})(\\d{3})(\\d{3})(\\d{2})", "$1.$2.$3-$4");

        } else if (documento != null && documento.length() == 14) {
            return documento.replaceAll("([A-Z0-9]{2})([A-Z0-9]{3})([A-Z0-9]{3})([A-Z0-9]{4})(\\d{2})", "$1.$2.$3/$4-$5");
        }

        return documento;
    }
}
