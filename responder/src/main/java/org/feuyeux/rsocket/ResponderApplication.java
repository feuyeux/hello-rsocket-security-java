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
@ComponentScan(basePackages = {"org.feuyeux.rsocket.api", "org.feuyeux.rsocket.basic"})
public class ResponderApplication {
    public static void main(String[] args) {
        SpringApplication.run(ResponderApplication.class);
    }
}