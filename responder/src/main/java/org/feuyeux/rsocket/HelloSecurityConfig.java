package org.feuyeux.rsocket;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.rsocket.RSocketStrategies;
import org.springframework.messaging.rsocket.annotation.support.RSocketMessageHandler;
import org.springframework.security.config.annotation.rsocket.EnableRSocketSecurity;
import org.springframework.security.config.annotation.rsocket.RSocketSecurity;

@Configuration
@EnableRSocketSecurity
public class HelloSecurityConfig {
    public static final String USER = "USER";
    public static final String ADMIN = "ADMIN";

    protected RSocketSecurity pattern(RSocketSecurity security) {
        return security.authorizePayload(authorize -> authorize
            .setup().permitAll()
            .route("hello-forget").authenticated()
            .route("hello-response").hasRole(USER)
            .route("hello-stream").hasRole(ADMIN)
            .route("hello-channel").hasAnyRole(USER, ADMIN)
            .anyRequest().authenticated()
            .anyExchange().permitAll()
        );
    }

    @Bean
    RSocketMessageHandler messageHandler(RSocketStrategies strategies) {
        RSocketMessageHandler mh = new RSocketMessageHandler();
        mh.getArgumentResolverConfigurer().addCustomResolver(
            new org.springframework.security.messaging.handler.invocation.reactive.AuthenticationPrincipalArgumentResolver());
        mh.setRSocketStrategies(strategies);
        return mh;
    }
}