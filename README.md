## hello-rsocket-security-java
> See more about the rsocket core hello demo: [hello-rsocket-java](https://github.com/feuyeux/hello-rsocket-java)


| access  | api  | description  | role  |
|:----|:----|:----|:----|
| setup  |        |                                        | ALL        |
| route |hello-forget   | fire-and-forget (no response)          | AUTHENTICATED|
| route |hello-response | request/response (stream of 1)         | USER       |
| route |hello-stream   | request/stream (finite stream of many) | ADMIN      |
| route |hello-channel  | channel (bi-directional streams)       | USER,ADMIN |

```sh
curl http://localhost:8989/api/hello-forget
curl http://localhost:8989/api/hello/1
curl http://localhost:8989/api/hello-stream
curl http://localhost:8989/api/hello-channel
```

### BASIC AUTH
```java
@ComponentScan(basePackages = {"org.feuyeux.rsocket.api", "org.feuyeux.rsocket.secure.basic"})
public class ResponderApplication {
    public static void main(String[] args) {
        SpringApplication.run(ResponderApplication.class);
    }
}
```

### JWT
```java
@ComponentScan(basePackages = {"org.feuyeux.rsocket.api", "org.feuyeux.rsocket.secure.jwt"})
public class ResponderApplication {
    public static void main(String[] args) {
        SpringApplication.run(ResponderApplication.class);
    }
}
```

### SSL
```java
@ComponentScan(basePackages = {"org.feuyeux.rsocket.api", "org.feuyeux.rsocket.secure.tls"})
public class ResponderApplication {
    public static void main(String[] args) {
        SpringApplication.run(ResponderApplication.class);
    }
}
```