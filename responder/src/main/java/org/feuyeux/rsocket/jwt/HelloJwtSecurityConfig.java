package org.feuyeux.rsocket.jwt;

import org.feuyeux.rsocket.HelloSecurityConfig;
import org.feuyeux.rsocket.utils.TokenUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.rsocket.EnableRSocketSecurity;
import org.springframework.security.config.annotation.rsocket.RSocketSecurity;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtReactiveAuthenticationManager;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.rsocket.core.PayloadSocketAcceptorInterceptor;

/**
 * https://docs.spring.io/spring-security/site/docs/5.3.1.RELEASE/reference/html5/#rsocket-authentication-jwt
 */
@Configuration
@EnableRSocketSecurity
public class HelloJwtSecurityConfig extends HelloSecurityConfig {
    /**
     * authorization
     */
    @Bean
    PayloadSocketAcceptorInterceptor authorization(RSocketSecurity rsocketSecurity) {
        RSocketSecurity security = pattern(rsocketSecurity)
                .jwt(jwtSpec -> {
                    try {
                        RSocketSecurity.JwtSpec spec = jwtSpec.authenticationManager(jwtReactiveAuthenticationManager(jwtDecoder()));
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
        return security.build();
    }

    @Bean
    public ReactiveJwtDecoder jwtDecoder() throws Exception {
        return TokenUtils.jwtDecoder();
    }

    @Bean
    public JwtReactiveAuthenticationManager jwtReactiveAuthenticationManager(ReactiveJwtDecoder reactiveJwtDecoder) {
        JwtReactiveAuthenticationManager authenticationManager = new JwtReactiveAuthenticationManager(reactiveJwtDecoder);
        JwtAuthenticationConverter authenticationConverter = new JwtAuthenticationConverter();
        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        grantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");
        authenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
        authenticationManager.setJwtAuthenticationConverter(new ReactiveJwtAuthenticationConverterAdapter(authenticationConverter));
        return authenticationManager;
    }
}