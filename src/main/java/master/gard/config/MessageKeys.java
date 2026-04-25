package master.gard.config;

public final class MessageKeys {

    private MessageKeys(){}

    public static final String CLIENTE_NAO_ENCONTRADO_TITLE = "error.cliente.nao-encontrado.title";
    public static final String CLIENTE_NAO_ENCONTRADO_DETAIL = "error.cliente.nao-encontrado.detail";

    public static final String CLIENTE_DOCUMENTO_DUPLICADO_TITLE = "error.cliente.documento-duplicado.title";
    public static final String CLIENTE_DOCUMENTO_DUPLICADO_DETAIL = "error.cliente.documento-duplicado.detail";

    public static final String CLIENTE_EMAIL_DUPLICADO_TITLE = "error.cliente.email-duplicado.title";
    public static final String CLIENTE_EMAIL_DUPLICADO_DETAIL = "error.cliente.email-duplicado.detail";

    public static final String CAMPOS_INVALIDOS_TITLE = "error.campos-invalidos.title";
    public static final String CAMPOS_INVALIDOS_DETAIL = "error.campos-invalidos.detail";

    public static final String CLAIM_SUB_INEXISTENTE_TITLE = "error.jwt.subject-nao-encontrado.title";
    public static final String CLAIM_SUB_INEXISTENTE_DETAIL = "error.jwt.subject-nao-encontrado.detail";

    public static final String CLIENTE_AUTENTICADO_SEM_CADASTRO_TITLE = "error.cliente.cadastro-nao-encontrado.title";
    public static final String CLIENTE_AUTENTICADO_SEM_CADASTRO_DETAIL = "error.cliente.cadastro-nao-encontrado.detail";

    public static final String CLIENTE_AUTENTICADO_JA_CADASTRADO_TITLE = "error.cliente.cadastro-existente.title";
    public static final String CLIENTE_AUTENTICADO_JA_CADASTRADO_DETAIL = "error.cliente.cadastro-existente.detail";
}
