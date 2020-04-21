package org.feuyeux.rsocket.secure.tls;

import io.netty.handler.ssl.SslContextBuilder;
import io.rsocket.RSocketFactory;
import io.rsocket.SocketAcceptor;
import io.rsocket.transport.ServerTransport;
import io.rsocket.transport.netty.server.CloseableChannel;
import io.rsocket.transport.netty.server.TcpServerTransport;
import lombok.Setter;
import org.springframework.boot.rsocket.netty.NettyRSocketServer;
import org.springframework.boot.rsocket.netty.NettyRSocketServerFactory;
import org.springframework.boot.rsocket.server.ServerRSocketFactoryProcessor;
import org.springframework.http.client.reactive.ReactorResourceFactory;
import org.springframework.util.Assert;
import reactor.core.publisher.Mono;
import reactor.netty.tcp.TcpServer;

import javax.net.ssl.KeyManagerFactory;
import java.io.File;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class NettyRSocketTlsServerFactory extends NettyRSocketServerFactory {
    @Setter
    private ReactorResourceFactory resourceFactory;
    @Setter
    private InetAddress address;
    @Setter
    private int port = 9898;
    @Setter
    private Duration lifecycleTimeout;
    private List<ServerRSocketFactoryProcessor> socketFactoryProcessors = new ArrayList<>();

    @Override
    public NettyRSocketServer create(SocketAcceptor socketAcceptor) {
        ServerTransport<CloseableChannel> transport = createTcpTransport();
        RSocketFactory.ServerRSocketFactory factory = RSocketFactory.receive();
        for (ServerRSocketFactoryProcessor processor : this.socketFactoryProcessors) {
            factory = processor.process(factory);
        }
        Mono<CloseableChannel> starter = factory.acceptor(socketAcceptor).transport(transport).start();
        return new NettyRSocketServer(starter, this.lifecycleTimeout);
    }

    private ServerTransport<CloseableChannel> createTcpTransport() {
        if (this.resourceFactory != null) {
            TcpServer tcpServer = TcpServer.create()
                    .secure(x -> {
                        x.sslContext(SslContextBuilder.forServer(
                                new File("/tmp/rsocket/certificate.pem"),
                                new File("/tmp/rsocket/key.pem")));
                    })
                    .runOn(this.resourceFactory.getLoopResources())
                    .addressSupplier(this::getListenAddress);
            return TcpServerTransport.create(tcpServer);
        }
        return TcpServerTransport.create(getListenAddress());
    }

    private InetSocketAddress getListenAddress() {
        if (this.address != null) {
            return new InetSocketAddress(this.address.getHostAddress(), this.port);
        }
        return new InetSocketAddress(this.port);
    }

    public void setSocketFactoryProcessors(
            Collection<? extends ServerRSocketFactoryProcessor> socketFactoryProcessors) {
        Assert.notNull(socketFactoryProcessors, "SocketFactoryProcessors must not be null");
        this.socketFactoryProcessors = new ArrayList<>(socketFactoryProcessors);
    }
}
