package com.github.hicolors.best.practices.exception;


import com.github.hicolors.best.practices.pojo.Result;
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
     * 业务域
     */
    private String domain;

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

    /**
     * 业务域标识自动取当前服务
     *
     * @param code    code
     * @param message message
     */
    public ExtensionException(Long code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * 指定业务域标识
     *
     * @param domain  domain
     * @param code    code
     * @param message message
     */
    public ExtensionException(String domain, Long code, String message) {
        this.domain = domain;
        this.code = code;
        this.message = message;
    }

    public ExtensionException(Result result) {
        this.domain = result.getDomain();
        this.code = result.getCode();
        this.message = result.getMsg();
        this.data = result.getData();
    }
}