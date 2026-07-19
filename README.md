# AI Image Edit
## 0.1.0 - SNAPSHOT

Biblioteca Java para integrar operações de **image-to-image** a modelos de inteligência artificial integrando ao módulo existente do Spring AI.

A proposta do projeto é receber uma imagem e instruções textuais, delegar a edição a um provedor de IA e devolver o resultado usando as abstrações de imagem do Spring AI. A implementação atual integra a API de edição de imagens da OpenAI; a separação entre `core` e `provider` prepara a biblioteca para receber outros provedores no futuro.

> O projeto está em desenvolvimento e usa a versão `0.1.0-SNAPSHOT`. A API pública ainda pode mudar antes da primeira versão estável.

## Requisitos

- Java 21 ou superior
- Spring Framework 7
- Spring AI 2
- Uma chave de API da OpenAI

## Instalação

Enquanto o artefato não estiver publicado em um repositório Maven, clone o projeto e instale o snapshot localmente:

```bash
mvn clean install
```

Depois, adicione a dependência ao projeto consumidor:

```xml
<dependency>
    <groupId>io.github.NicolasBrum</groupId>
    <artifactId>ai-image-edit</artifactId>
    <version>0.1.0-SNAPSHOT</version>
</dependency>
```

## Como funciona

O fluxo principal da biblioteca é:

```text
imagem + instruções + opções
              |
              v
      AiImageEditPrompt
              |
              v
       AiImageEditModel
              |
              v
   cliente do provedor de IA
              |
              v
 Spring AI ImageResponse
```

As responsabilidades estão separadas da seguinte forma:

- `AiImageEditPrompt` representa a imagem, as instruções e as opções da edição.
- `AiImageEditModel` é a API usada pela aplicação para solicitar uma edição.
- `AiImageEditClient` define o contrato comum dos clientes de provedores.
- `OpenAiImageEditClient` transforma o prompt em uma requisição multipart para um modelo específico de IA.
- `OpenAiImageResultAdapter` converte a resposta específica do modelo para o modelo comum da biblioteca.

A chamada é síncrona: `AiImageEditModel.call(...)` aguarda o provedor terminar a edição e retorna um `ImageResponse` do Spring AI.

## Uso com Spring

A configuração fornecida pela biblioteca registra um `OpenAiImageEditClient` e um `AiImageEditModel`. Importe-a na aplicação:

```java
import io.github.nicolasbrum.config.OpenAiImageEditConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(OpenAiImageEditConfiguration.class)
public class ImageEditConfiguration {
}
```

Configure a chave e, se necessário, os timeouts:

```yaml
spring:
  ai:
    openai:
      api-key: ${OPENAI_API_KEY}

ai:
  image:
    edit:
      client:
        openai:
          connection-timeout: PT30S
          read-timeout: PT3M
```

A chave também pode ser configurada em `ai.image.edit.client.openai.api-key`. Quando as duas propriedades existem, `spring.ai.openai.api-key` tem prioridade.

Os timeouts usam o formato ISO-8601 de `java.time.Duration`. Exemplos: `PT15S` para 15 segundos e `PT2M` para 2 minutos.

Com o modelo registrado, construa o prompt e faça a chamada:

```java
import io.github.nicolasbrum.core.AiImageEditModel;
import io.github.nicolasbrum.core.AiImageEditPrompt;
import org.springframework.ai.image.ImageOptions;
import org.springframework.ai.image.ImageOptionsBuilder;
import org.springframework.ai.image.ImageResponse;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

@Service
public class ProductImageService {

    private final AiImageEditModel imageEditModel;

    public ProductImageService(AiImageEditModel imageEditModel) {
        this.imageEditModel = imageEditModel;
    }

    public ImageResponse removeBackground(String imagePath) {
        ImageOptions options = ImageOptionsBuilder.builder()
                .model("gpt-image-1")
                .n(1)
                .build();

        AiImageEditPrompt prompt = AiImageEditPrompt.builder()
                .resource(new FileSystemResource(imagePath))
                .mediaType(MediaType.IMAGE_PNG)
                .instruction("Remova o fundo da imagem.")
                .instruction("Preserve o produto e suas cores originais.")
                .imageOptions(options)
                .build();

        return imageEditModel.call(prompt);
    }
}
```

O `MediaType` deve representar o conteúdo real do arquivo enviado. Por exemplo, use `MediaType.IMAGE_JPEG` para uma imagem JPEG.

## Uso sem configuração Spring

O cliente também pode ser construído diretamente. Isso é útil em aplicações que não usam um contexto Spring ou que preferem controlar a criação dos objetos:

```java
import io.github.nicolasbrum.core.AiImageEditModel;
import io.github.nicolasbrum.core.AiImageEditPrompt;
import io.github.nicolasbrum.core.DefaultAiImageEditModel;
import io.github.nicolasbrum.provider.openai.OpenAiImageEditClient;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;

import java.time.Duration;

String apiKey = System.getenv("OPENAI_API_KEY");

var client = new OpenAiImageEditClient(
        apiKey,
        Duration.ofSeconds(30),
        Duration.ofMinutes(3)
);

AiImageEditModel model = new DefaultAiImageEditModel(client);

var prompt = AiImageEditPrompt.builder()
        .resource(new FileSystemResource("entrada.png"))
        .mediaType(MediaType.IMAGE_PNG)
        .instruction("Transforme a cena em uma ilustração em aquarela.")
        .build();

var response = model.call(prompt);
```

Nunca coloque a chave da API diretamente no código-fonte ou em arquivos versionados.

## O que pode ser definido pelo usuário

### Conteúdo da edição

| Campo | Obrigatório | Comportamento |
| --- | --- | --- |
| `resource` | Sim | Imagem de entrada representada por um `Resource` do Spring. |
| `mediaType` | Sim | Tipo MIME correspondente à imagem enviada. |
| `instruction` / `instructions` | Sim | Uma ou mais instruções. Elas são unidas com quebra de linha antes do envio. |
| `imageOptions` | Não | Opções do Spring AI. Quando omitidas, a biblioteca cria opções padrão. |

O builder rejeita prompts sem imagem, tipo de mídia ou instruções.

### Opções enviadas à OpenAI

Na implementação atual, o cliente OpenAI utiliza estas opções:

| Opção | Default | Descrição |
| --- | --- | --- |
| `model` | `gpt-image-1` | Modelo usado para editar a imagem. |
| `n` | `1` | Quantidade de imagens solicitadas. |

Embora `ImageOptions` também possua largura, altura, formato de resposta e estilo, esses valores **ainda não são enviados** pelo `OpenAiImageEditClient`. Defini-los nesta versão não altera a requisição. Essa distinção evita que uma configuração aparentemente aceita e produza expectativas incorretas.

### Configuração do cliente HTTP

| Propriedade | Default | Descrição |
| --- | --- | --- |
| `spring.ai.openai.api-key` | — | Chave da OpenAI; tem prioridade sobre a propriedade alternativa. |
| `ai.image.edit.client.openai.api-key` | — | Propriedade alternativa para a chave da OpenAI. |
| `ai.image.edit.client.openai.connection-timeout` | `PT30S` | Tempo máximo para estabelecer a conexão. |
| `ai.image.edit.client.openai.read-timeout` | `PT3M` | Tempo máximo de espera pela resposta. |

### Observação: Dependendo do tamanho da imagem, será necessário alterar a configuração de tamanho suportado à arquivos no framework.

## Trabalhando com o resultado

O retorno é um `ImageResponse`. Cada item de `getResults()` contém uma imagem com o conteúdo Base64 em `getB64Json()`:

```java
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;

var generation = response.getResult();

if (generation != null && generation.getOutput().getB64Json() != null) {
    byte[] imageBytes = Base64.getDecoder()
            .decode(generation.getOutput().getB64Json());

    Files.write(Path.of("imagem-editada.png"), imageBytes);
}
```

Os metadados da resposta podem incluir:

- instante de criação informado pelo provedor;
- `quality`;
- `outputFormat`;
- `size`.

Esses campos dependem do conteúdo retornado pela OpenAI e podem não estar presentes em todas as respostas.

## Escopo atual

Nesta versão, a biblioteca:

- suporta o provedor OpenAI;
- aceita uma imagem de entrada por chamada;
- devolve imagens codificadas em Base64;
- executa chamadas síncronas;
- envia `model` e `n` como opções da edição.

## Evolução do projeto

A arquitetura separa o contrato comum dos detalhes da OpenAI para permitir que novos provedores sejam adicionados sem alterar a forma como a aplicação chama `AiImageEditModel`. As próximas evoluções naturais incluem ampliar as opções de edição, melhorar o tratamento de erros e oferecer novos adaptadores de provedores.

