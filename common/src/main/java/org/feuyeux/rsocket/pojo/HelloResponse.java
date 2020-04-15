package org.feuyeux.rsocket.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author feuyeux@gmail.com
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class HelloResponse {
    private String id;
    private String value;
}
