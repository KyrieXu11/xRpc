package com.kyriexu.enity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author KyrieXu
 * @since 2020/8/20 19:21
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Request implements Serializable {
    private static final long serialVersionUID = -2914888719171132007L;
    private String id;
    private String methodName;
    private String className;
    private Object[] args;
}
