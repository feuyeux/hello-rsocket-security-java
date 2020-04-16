package org.feuyeux.rsocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author feuyeux@gmail.com
 */
@Slf4j
@SpringBootApplication
//@ComponentScan(basePackages = {"org.feuyeux.rsocket.api", "org.feuyeux.rsocket.basic"})
@ComponentScan(basePackages = {"org.feuyeux.rsocket.api", "org.feuyeux.rsocket.jwt"})
public class RequesterApplication {
    public static void main(String[] args) {
        SpringApplication.run(RequesterApplication.class);
    }
}

