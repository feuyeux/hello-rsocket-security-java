package org.feuyeux.rsocket.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;

/**
 * Generates an admin token and user token for the demo.
 */
public class TokenUtils {
    private static final String SECRET_KEY = "DIO_20200416_RSOCKET_SECURITY@SDS";
    private static final MacAlgorithm MAC_ALGORITHM = MacAlgorithm.HS256;

    public static String generate(String name, String role) {
        Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
        return JWT.create()
                .withJWTId(UUID.randomUUID().toString())
                .withSubject(name)
                .withClaim("scope", role)
                .withExpiresAt(Date.from(Instant.now().plus(120, ChronoUnit.MINUTES)))
                .sign(algorithm);
    }

    public static ReactiveJwtDecoder jwtDecoder() throws Exception {
        Mac mac = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKey = new SecretKeySpec(SECRET_KEY.getBytes(), mac.getAlgorithm());
        return NimbusReactiveJwtDecoder.withSecretKey(secretKey)
                .macAlgorithm(MAC_ALGORITHM)
                .build();
    }
}
