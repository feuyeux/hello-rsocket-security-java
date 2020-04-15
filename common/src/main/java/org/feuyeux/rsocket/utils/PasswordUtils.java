package org.feuyeux.rsocket.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordUtils {
    public static final String PW_RAW = "pw";
    public static final String PW_ENCODED = "$2a$06$Kt78T5rguhoVdgc5nsQuZeUE/RXQOjgdZ1DGWD8YzjBGiQ10JHu8W";
    public static final BCryptPasswordEncoder B_CRYPT_PASSWORD_ENCODER = new BCryptPasswordEncoder(6);

    public static String encode(String pass) {
        return B_CRYPT_PASSWORD_ENCODER.encode(pass);
    }

    public static boolean matches(String raw, String encoded) {
        return B_CRYPT_PASSWORD_ENCODER.matches(raw, encoded);
    }
}