package io.github.nicolasbrum.config;

import io.github.nicolasbrum.core.AiImageEditModel;
import io.github.nicolasbrum.core.DefaultAiImageEditModel;
import io.github.nicolasbrum.provider.openai.OpenAiImageEditClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration(proxyBeanMethods = false)
public class OpenAiImageEditConfiguration {
    @Bean
    public OpenAiImageEditClient openAiImageEditClient(
            @Value("${spring.ai.openai.api-key:${ai.image.edit.client.openai.api-key}}")
            String apiKey,
            @Value("${ai.image.edit.client.openai.con.timeout:${ai.image.edit.client.openai.con.timeout=30s}}")
            Duration connectionTimeout,
            @Value("${ai.image.edit.client.openai.read.timeout:${ai.image.edit.client.openai.read.timeout=3m}}")
            Duration readTimeout
    ) {
        return new OpenAiImageEditClient(apiKey, connectionTimeout, readTimeout);
    }

    @Bean
    public AiImageEditModel aiImageEditModel(
            OpenAiImageEditClient client) {

        return new DefaultAiImageEditModel(client);
    }
}
