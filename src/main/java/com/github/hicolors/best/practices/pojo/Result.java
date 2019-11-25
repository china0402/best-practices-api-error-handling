package com.github.hicolors.best.practices.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Result
 *
 * @author Weichao Li (liweichao0102@gmail.com)
 * @since 2019-08-11
 */
@Data
@AllArgsConstructor
@ApiModel("统一 Response 返回值")
public class Result<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final long SUCCESS_CODE = 200L;

    public static final String DEFAULT_SUCCESS_MESSAGE = "success";

    @ApiModelProperty(name = "业务域或应用标识", notes = "仅当产生错误时会赋值该字段")
    private String domain;

    @ApiModelProperty(name = "结果码", notes = "正确响应时该值为 Result#SUCCESS_CODE，错误响应时为错误代码")
    private long code;

    @ApiModelProperty(name = "人工可读的消息", notes = "正确响应时该值为 Result#DEFAULT_SUCCESS_MESSAGE，错误响应时为错误信息")
    private String msg;

    @ApiModelProperty(name = "响应体", notes = "正确响应时该值会被使用")
    private T data;

    /**
     * 当验证错误时，各项具体的错误信息
     */
    @ApiModelProperty("错误信息")
    private List<Error> errors;

    public Result(T data) {
        this.setData(data);
        this.setCode(SUCCESS_CODE);
        this.setMsg(DEFAULT_SUCCESS_MESSAGE);
    }

    public Result() {
        this.setCode(SUCCESS_CODE);
        this.setMsg(DEFAULT_SUCCESS_MESSAGE);
    }

    public void addError(String name, String message) {
        if (this.errors == null) {
            this.errors = new ArrayList<>();
        }
        this.errors.add(new Error(name, message));
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @ApiModel("统一 Response 返回值中错误信息的模型")
    class Error {

        @ApiModelProperty(name = "错误项", notes = "错误的具体项")
        private String name;

        @ApiModelProperty(name = "错误项说明", notes = "错误的具体项说明")
        private String message;
    }
}