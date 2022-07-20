package com.hyx.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author hyx
 **/

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class LogLine {
    private Long id;
    private Byte type;
    private Integer keyLength;
    private String key;
    private Integer valueLength;
    private String value;
}
