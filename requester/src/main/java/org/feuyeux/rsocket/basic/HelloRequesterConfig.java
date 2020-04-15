package org.feuyeux.rsocket.basic;

import io.rsocket.metadata.WellKnownMimeType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.security.rsocket.metadata.SimpleAuthenticationEncoder;
import org.springframework.security.rsocket.metadata.UsernamePasswordMetadata;
import org.springframework.util.MimeType;
import org.springframework.util.MimeTypeUtils;

@Configuration
public class HelloRequesterConfig {
    private final MimeType mimeType = MimeTypeUtils.parseMimeType(WellKnownMimeType.MESSAGE_RSOCKET_AUTHENTICATION.getString());
    private final UsernamePasswordMetadata userCredentials = new UsernamePasswordMetadata("user", "pw");
    private final UsernamePasswordMetadata adminCredentials = new UsernamePasswordMetadata("admin", "pw");

    @Bean
    RSocketRequester rSocketRequester(RSocketRequester.Builder builder) {
        return builder
                //.setupMetadata(userCredentials, this.mimeType)
                .setupMetadata(adminCredentials, this.mimeType)
                .rsocketStrategies(strategiesBuilder -> {
                    strategiesBuilder.encoder(new SimpleAuthenticationEncoder());
                })
                .connectTcp("localhost", 7878)
                .block();
    }
}
