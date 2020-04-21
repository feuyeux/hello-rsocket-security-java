package org.feuyeux.rsocket.secure.tls;

import io.rsocket.metadata.WellKnownMimeType;
import org.feuyeux.rsocket.utils.TokenUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.security.rsocket.metadata.BearerTokenMetadata;
import org.springframework.util.MimeType;
import org.springframework.util.MimeTypeUtils;

import static org.feuyeux.rsocket.pojo.HelloRole.ADMIN;
import static org.feuyeux.rsocket.pojo.HelloRole.USER;

@Configuration
public class HelloRequesterConfig {
    @Bean
    RSocketRequester rSocketRequester(RSocketRequester.Builder builder) {
        return builder
                .connectTcp("localhost", 7878)
                .block();
    }
}
