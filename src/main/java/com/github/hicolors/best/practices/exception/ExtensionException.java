package com.github.hicolors.best.practices.exception;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 扩展异常
 *
 * @author Weichao Li (liweichao0102@gmail.com)
 * @since 2019-08-11
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ExtensionException extends RuntimeException {

    /**
     * 业务异常码 ( 详情参加文档说明 )
     */
    private Long code;

    /**
     * 业务异常信息
     */
    private String message;

    /**
     * 额外数据，可支持扩展
     */
    private Object data;

    /**
     * cause
     */
    private Throwable cause;

    public ExtensionException(Long code, String message) {
        this.code = code;
        this.message = message;
    }
}