package org.feuyeux.rsocket.basic;

import org.feuyeux.rsocket.HelloSecurityConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.rsocket.EnableRSocketSecurity;
import org.springframework.security.config.annotation.rsocket.RSocketSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.rsocket.core.PayloadSocketAcceptorInterceptor;

import static org.feuyeux.rsocket.pojo.HelloRole.ADMIN;
import static org.feuyeux.rsocket.pojo.HelloRole.USER;
import static org.feuyeux.rsocket.utils.PasswordUtils.B_CRYPT_PASSWORD_ENCODER;
import static org.feuyeux.rsocket.utils.PasswordUtils.PW_ENCODED;

/**
 * https://docs.spring.io/spring-security/site/docs/5.3.1.RELEASE/reference/html5/#rsocket-authentication-simple
 */
@Configuration
@EnableRSocketSecurity
public class HelloBasicSecurityConfig extends HelloSecurityConfig {
    /**
     * authentication
     */
    @Bean
    MapReactiveUserDetailsService authentication() {
        String pw = PW_ENCODED;
        UserDetails user = User.withUsername("user")
                .password(pw)
                .roles(USER)
                .build();

        UserDetails admin = User.withUsername("admin")
                .password(pw)
                .roles(ADMIN)
                .build();

        return new MapReactiveUserDetailsService(user, admin);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return B_CRYPT_PASSWORD_ENCODER;
    }

    /**
     * authorization
     */
    @Bean
    PayloadSocketAcceptorInterceptor authorization(RSocketSecurity rsocketSecurity) {
        RSocketSecurity security = pattern(rsocketSecurity).simpleAuthentication(Customizer.withDefaults());
        return security.build();
    }
}