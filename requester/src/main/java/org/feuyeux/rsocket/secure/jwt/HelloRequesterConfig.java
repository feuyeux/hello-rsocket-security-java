package org.feuyeux.rsocket.secure.jwt;

import io.rsocket.metadata.WellKnownMimeType;
import org.feuyeux.rsocket.pojo.HelloUser;
import org.feuyeux.rsocket.utils.TokenUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.security.rsocket.metadata.BearerTokenMetadata;
import org.springframework.util.MimeType;
import org.springframework.util.MimeTypeUtils;

import static org.feuyeux.rsocket.pojo.HelloRole.ADMIN;
import static org.feuyeux.rsocket.pojo.HelloRole.USER;

/**
 * https://github.com/rsocket/rsocket/blob/master/Extensions/Security/Authentication.md
 * https://github.com/rsocket/rsocket/blob/master/Extensions/Security/WellKnownAuthTypes.md
 */
@Configuration
public class HelloRequesterConfig {
    //message/x.rsocket.authentication.v0
    private final MimeType mimeType2 = MimeTypeUtils.parseMimeType(WellKnownMimeType.MESSAGE_RSOCKET_AUTHENTICATION.getString());
    //message/x.rsocket.authentication.bearer.v0
    private final MimeType mimeType = BearerTokenMetadata.BEARER_AUTHENTICATION_MIME_TYPE;

    @Bean
    RSocketRequester rSocketRequester(RSocketRequester.Builder builder) {
        HelloUser user = HelloUser.builder().userId("user").password("868").role(USER).build();
        HelloUser admin = HelloUser.builder().userId("admin").password("868").role(ADMIN).build();

        String userToken = TokenUtils.generateAccessToken(user).getToken();
        String adminToken = TokenUtils.generateAccessToken(admin).getToken();

        return builder
                .setupMetadata(userToken, this.mimeType)
                //.setupMetadata(userToken, this.mimeType)
                //.setupMetadata(adminToken, this.mimeType)
                .connectTcp("localhost", 7878)
                .block();
    }
}
