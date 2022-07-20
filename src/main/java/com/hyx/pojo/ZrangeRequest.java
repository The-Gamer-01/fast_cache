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
public class ZrangeRequest {
    private Integer min_score;
    private Integer max_score;
}
