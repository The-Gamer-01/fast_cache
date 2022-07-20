package com.hyx.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author hyx
 **/

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class InsertRequest {
    private String key;
    private String value;
}
