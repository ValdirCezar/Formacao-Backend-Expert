package br.com.valdircezar.helpdeskbff.config;

import br.com.valdircezar.helpdeskbff.decoder.RetrieveMessageErrorDecoder;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig {

    @Bean
    ErrorDecoder getDecoder() {
        return new RetrieveMessageErrorDecoder();
    }
}
