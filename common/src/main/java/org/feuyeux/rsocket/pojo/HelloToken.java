package org.feuyeux.rsocket.pojo;

import lombok.Data;

@Data
public class HelloToken {
    private String accessToken;
    private String refreshToken;
}
