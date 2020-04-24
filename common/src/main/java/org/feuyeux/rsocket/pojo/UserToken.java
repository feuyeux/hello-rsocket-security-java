package org.feuyeux.rsocket.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserToken {
    private String tokenId;
    private String token;
    private HelloUser user;
}
