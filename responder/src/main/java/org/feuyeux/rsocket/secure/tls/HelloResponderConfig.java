package org.feuyeux.rsocket.secure.tls;

import io.rsocket.SocketAcceptor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.rsocket.RSocketProperties;
import org.springframework.boot.context.properties.PropertyMapper;
import org.springframework.boot.rsocket.context.RSocketServerBootstrap;
import org.springframework.boot.rsocket.server.RSocketServerFactory;
import org.springframework.boot.rsocket.server.ServerRSocketFactoryProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorResourceFactory;
import org.springframework.messaging.rsocket.annotation.support.RSocketMessageHandler;

import java.util.stream.Collectors;

/**
 * override
 * org.springframework.boot.autoconfigure.rsocket.RSocketServerAutoConfiguration.EmbeddedServerAutoConfiguration
 */
@Configuration
public class HelloResponderConfig {
    @Bean
    RSocketServerFactory rSocketServerFactory(RSocketProperties properties, ReactorResourceFactory resourceFactory,
                                              ObjectProvider<ServerRSocketFactoryProcessor> processors) {
        NettyRSocketTlsServerFactory factory = new NettyRSocketTlsServerFactory();
        factory.setResourceFactory(resourceFactory);
        factory.setTransport(properties.getServer().getTransport());
        PropertyMapper map = PropertyMapper.get().alwaysApplyingWhenNonNull();
        map.from(properties.getServer().getAddress()).to(factory::setAddress);
        map.from(properties.getServer().getPort()).to(factory::setPort);
        factory.setSocketFactoryProcessors(processors.orderedStream().collect(Collectors.toList()));
        return factory;
    }

    @Bean
    RSocketServerBootstrap rSocketServerBootstrap(NettyRSocketTlsServerFactory rSocketServerFactory,
                                                  RSocketMessageHandler rSocketMessageHandler) {
        SocketAcceptor socketAcceptor = rSocketMessageHandler.responder();
        return new RSocketServerBootstrap(rSocketServerFactory, socketAcceptor);
    }
}
