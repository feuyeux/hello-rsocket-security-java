package org.feuyeux.rsocket.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

/**
 * @author feuyeux@gmail.com
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class HelloRequests {
    private List<String> ids;
}
