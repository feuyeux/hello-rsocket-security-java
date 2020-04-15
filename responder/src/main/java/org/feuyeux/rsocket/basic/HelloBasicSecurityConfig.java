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
import org.springframework.security.rsocket.core.PayloadSocketAcceptorInterceptor;

/**
 * https://docs.spring.io/spring-security/site/docs/5.3.1.RELEASE/reference/html5/#rsocket
 */
@Configuration
@EnableRSocketSecurity
public class HelloBasicSecurityConfig extends HelloSecurityConfig {
    /**
     * authentication
     */
    @Bean
    MapReactiveUserDetailsService authentication() {
        UserDetails user = User.withDefaultPasswordEncoder()
                .username("user")
                .password("pw")
                .roles(USER_ROLE)
                .build();

        UserDetails admin = User.withDefaultPasswordEncoder()
                .username("admin")
                .password("pw")
                .roles(ADMIN_ROLE)
                .build();

        return new MapReactiveUserDetailsService(user, admin);
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