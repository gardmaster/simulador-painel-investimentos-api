CREATE TABLE clientes (
                          id BIGINT IDENTITY(1,1) NOT NULL,
                          nome NVARCHAR(255) NOT NULL,
                          documento NVARCHAR(255) NOT NULL,
                          email NVARCHAR(255) NOT NULL,
                          perfil_risco NVARCHAR(30) NOT NULL,
                          data_criacao DATETIME2(6) NOT NULL,
                          data_atualizacao DATETIME2(6) NOT NULL,
                          CONSTRAINT pk_clientes PRIMARY KEY (id),
                          CONSTRAINT uk_clientes_documento UNIQUE (documento),
                          CONSTRAINT uk_clientes_email UNIQUE (email),
                          CONSTRAINT ck_clientes_perfil_risco CHECK (
                              perfil_risco IN ('CONSERVADOR', 'MODERADO', 'ARROJADO', 'AGRESSIVO', 'NAO_CLASSIFICADO')
                              )
);

CREATE TABLE produtos (
                          id BIGINT IDENTITY(1,1) NOT NULL,
                          nome NVARCHAR(255) NOT NULL,
                          tipo_produto NVARCHAR(50) NOT NULL,
                          produto_risco NVARCHAR(30) NOT NULL,
                          rentabilidade_mensal FLOAT NOT NULL,
                          data_vencimento DATETIME2(6) NULL,
                          data_criacao DATETIME2(6) NOT NULL,
                          data_atualizacao DATETIME2(6) NOT NULL,
                          CONSTRAINT pk_produtos PRIMARY KEY (id),
                          CONSTRAINT ck_produtos_tipo_produto CHECK (
                              tipo_produto IN (
                                               'CDB', 'LCI', 'LCA', 'TESOURO_DIRETO_SELIC',
                                               'FIA', 'FII', 'CRI', 'CRA', 'DEBENTURE'
                                  )
                              ),
                          CONSTRAINT ck_produtos_produto_risco CHECK (
                              produto_risco IN ('BAIXISSIMO', 'BAIXO', 'MEDIO', 'ALTO', 'ALTISSIMO')
                              )
);

CREATE TABLE investimentos (
                               id BIGINT IDENTITY(1,1) NOT NULL,
                               produto_id BIGINT NOT NULL,
                               cliente_id BIGINT NOT NULL,
                               valor DECIMAL(19,2) NOT NULL,
                               data_investimento DATETIME2(6) NOT NULL,
                               CONSTRAINT pk_investimentos PRIMARY KEY (id),
                               CONSTRAINT fk_investimentos_produto FOREIGN KEY (produto_id) REFERENCES produtos (id),
                               CONSTRAINT fk_investimentos_cliente FOREIGN KEY (cliente_id) REFERENCES clientes (id)
);

CREATE TABLE simulacoes (
                            id BIGINT IDENTITY(1,1) NOT NULL,
                            produto_id BIGINT NOT NULL,
                            cliente_id BIGINT NOT NULL,
                            valor_investido DECIMAL(19,2) NOT NULL,
                            valor_final DECIMAL(19,2) NOT NULL,
                            prazo_meses INT NOT NULL,
                            data_simulacao DATETIME2(6) NOT NULL,
                            CONSTRAINT pk_simulacoes PRIMARY KEY (id),
                            CONSTRAINT fk_simulacoes_produto FOREIGN KEY (produto_id) REFERENCES produtos (id),
                            CONSTRAINT fk_simulacoes_cliente FOREIGN KEY (cliente_id) REFERENCES clientes (id)
);

CREATE INDEX idx_investimentos_produto_id ON investimentos (produto_id);
CREATE INDEX idx_investimentos_cliente_id ON investimentos (cliente_id);
CREATE INDEX idx_simulacoes_produto_id ON simulacoes (produto_id);
CREATE INDEX idx_simulacoes_cliente_id ON simulacoes (cliente_id);
