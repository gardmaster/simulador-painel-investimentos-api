package master.gard.config;

import jakarta.ws.rs.core.Application;
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Contact;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.servers.Server;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@OpenAPIDefinition(
        info = @Info(
                title = "Painel de Investimentos API",
                version = "1.0",
                description = """
                        API RESTful para simulação de investimentos baseado em perfil dinâmico do investidor.
                        
                        ## Funcionalidades
                        - Cadastro e gestão de clientes
                        - Simulação de investimentos
                        - Recomendação de investimentos baseados em perfil de risco
                        - Perfil de risco dinânico com base em comportamento de investimentos e/ou simulações
                        - Telemetria de dados
                        """,
                contact = @Contact(
                        name = "Giovanni Duarte",
                        email = "garduarte@teste.com"
                )
        ),
        servers = {
                @Server(url = "http://localhost:8080", description = "Ambiente de Desenvolvimento")
        },
        tags = {
                @Tag(name = "Clientes", description = "Operações de gerenciamento de clientes")
        }
)
public class OpenAiConfig extends Application {
}
