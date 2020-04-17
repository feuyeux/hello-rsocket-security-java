package org.feuyeux.rsocket.secure;

import org.springframework.messaging.rsocket.RSocketStrategies;
import org.springframework.messaging.rsocket.annotation.support.RSocketMessageHandler;
import org.springframework.security.config.annotation.rsocket.RSocketSecurity;

import static org.feuyeux.rsocket.pojo.HelloRole.ADMIN;
import static org.feuyeux.rsocket.pojo.HelloRole.USER;

public class HelloSecurityConfig {
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

    protected RSocketMessageHandler getMessageHandler(RSocketStrategies strategies) {
        RSocketMessageHandler mh = new RSocketMessageHandler();
        mh.getArgumentResolverConfigurer().addCustomResolver(
                new org.springframework.security.messaging.handler.invocation.reactive.AuthenticationPrincipalArgumentResolver());
        mh.setRSocketStrategies(strategies);
        return mh;
    }
}