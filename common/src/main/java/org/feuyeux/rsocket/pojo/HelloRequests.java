package org.feuyeux.rsocket.pojo;

import java.util.List;

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
public class HelloRequests {
    private List<String> ids;
}
