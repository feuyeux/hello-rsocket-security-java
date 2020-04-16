package org.feuyeux.rsocket.jwt;

import io.rsocket.metadata.WellKnownMimeType;
import org.feuyeux.rsocket.utils.TokenUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.util.MimeType;
import org.springframework.util.MimeTypeUtils;

import static org.feuyeux.rsocket.pojo.HelloRole.ADMIN;
import static org.feuyeux.rsocket.pojo.HelloRole.USER;

@Configuration
public class HelloRequesterConfig {
    private final String userToken = TokenUtils.generate("user", USER);
    private final String adminToken = TokenUtils.generate("admin", ADMIN);

    private final MimeType mimeType = MimeTypeUtils.parseMimeType(WellKnownMimeType.MESSAGE_RSOCKET_AUTHENTICATION.getString());

    @Bean
    RSocketRequester rSocketRequester(RSocketRequester.Builder builder) {
        return builder
                .setupMetadata(userToken, this.mimeType)
                //.setupMetadata(userToken, this.mimeType)
                //.setupMetadata(adminToken, this.mimeType)
                .connectTcp("localhost", 7878)
                .block();
    }
}
