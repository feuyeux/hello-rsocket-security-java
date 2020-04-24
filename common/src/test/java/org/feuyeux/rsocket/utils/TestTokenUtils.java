package org.feuyeux.rsocket.utils;

import org.feuyeux.rsocket.pojo.HelloUser;
import org.junit.Test;

import static org.feuyeux.rsocket.pojo.HelloRole.ADMIN;
import static org.feuyeux.rsocket.pojo.HelloRole.USER;

public class TestTokenUtils {
    @Test
    public void test() {
        HelloUser user = HelloUser.builder().userId("user").password("868").role(USER).build();
        HelloUser admin = HelloUser.builder().userId("user").password("868").role(ADMIN).build();

        String userToken = TokenUtils.generateAccessToken(user).getToken();
        String adminToken = TokenUtils.generateAccessToken(admin).getToken();
        System.out.println();
        System.out.println("Generated Tokens");
        System.out.println("================");
        System.out.println("Admin: \n" + adminToken);
        System.out.println();
        System.out.println("User: \n" + userToken);
        System.out.println();
    }
}
