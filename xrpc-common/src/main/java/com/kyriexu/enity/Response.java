package com.kyriexu.enity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author KyrieXu
 * @since 2020/8/24 12:23
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Response {
    private Class<?> returnType;
    private Object result;
}
