package org.feuyeux.rsocket.ultimate;

import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.SslProvider;
import io.netty.handler.ssl.util.SelfSignedCertificate;
import io.rsocket.ConnectionSetupPayload;
import io.rsocket.RSocket;
import io.rsocket.RSocketFactory;
import io.rsocket.SocketAcceptor;
import io.rsocket.transport.netty.server.TcpServerTransport;
import io.rsocket.transport.netty.server.WebsocketServerTransport;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.netty.tcp.TcpServer;

import javax.net.ssl.SSLException;
import java.io.File;
import java.security.PrivateKey;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.time.Duration;

@Slf4j
public class RSocketServer {
    public static final String HOST = "localhost";
    public static final int PORT = 7878;
    public static final String[] protocols = new String[]{"TLSv1.2"};

    public static void main(String[] args) throws InterruptedException, SSLException, CertificateException {
        TcpServerTransport tcpTransport = TcpServerTransport.create(HOST, PORT);
        WebsocketServerTransport wsTransport = WebsocketServerTransport.create(HOST, PORT);

        File certFile = new File("/Users/han/shop/cert.pem");
        File keyFile = new File("/Users/han/shop/key.pem");
        SelfSignedCertificate selfSignedCertificate = new SelfSignedCertificate();
        PrivateKey privateKey = selfSignedCertificate.key();
        X509Certificate certificate = selfSignedCertificate.cert();

        final SslContext sslContext = SslContextBuilder
                //.forServer(certFile, keyFile)
                .forServer(privateKey, certificate)
                .protocols(protocols).sslProvider(SslProvider.JDK).build();
        TcpServer tcpServer = TcpServer.create().host(HOST).port(PORT).secure(spec -> {
            spec.sslContext(sslContext);
        });
        TcpServerTransport tlsTransport = TcpServerTransport.create(tcpServer);

        RSocketFactory.receive()
                .errorConsumer(throwable -> {
                    log.error("", throwable);
                })
                .resume()
                .resumeSessionDuration(Duration.ofSeconds(60))
                .acceptor(new HelloSocketAcceptor())
                .transport(tlsTransport)
                .start()
                .subscribe();
        Thread.currentThread().join();
    }

    @Slf4j
    static class HelloSocketAcceptor implements SocketAcceptor {
        @Override
        public Mono<RSocket> accept(ConnectionSetupPayload setup, RSocket sendingSocket) {
            log.debug("Received connection with setup payload: [{}] and meta-data: [{}]",
                    setup.getDataUtf8(), setup.getMetadataUtf8());
            return Mono.just(new HelloRSocket());
        }
    }
}
