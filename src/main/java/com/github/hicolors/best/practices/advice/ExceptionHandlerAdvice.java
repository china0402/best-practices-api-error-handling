package com.github.hicolors.best.practices.advice;

import com.github.hicolors.best.practices.exception.ExtensionException;
import com.github.hicolors.best.practices.pojo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.text.MessageFormat;
import java.util.List;
import java.util.Objects;

/**
 * ExceptionHandlerAdvice
 *
 * @author Weichao Li (liweichao0102@gmail.com)
 * @since 2019/11/25
 */
@RestControllerAdvice
@Slf4j
public class ExceptionHandlerAdvice {

    @Value("${spring.application.domain:${spring.application.name:unknown-spring-boot}}")
    private String domain;

    /**
     * 针对业务异常的处理
     *
     * @param exception 业务异常
     * @param request   http request
     * @param response  http response
     * @return 异常处理结果
     */
    @ExceptionHandler(value = ExtensionException.class)
    @SuppressWarnings("unchecked")
    public Result extensionException(ExtensionException exception,
                                     HttpServletRequest request, HttpServletResponse response) {
        log.warn("请求发生了预期异常，出错的 url [{}]，出错的描述为 [{}]",
                request.getRequestURL().toString(), exception.getMessage());
        Result result = new Result();
        result.setDomain(StringUtils.isEmpty(exception.getDomain()) ? domain : exception.getDomain());
        result.setCode(exception.getCode());
        result.setMsg(exception.getMessage());
        Object data = exception.getData();
        if (Objects.nonNull(data) && data instanceof List) {
            if (((List) data).size() > 0 && (((List) data).get(0) instanceof Result.Error)) {
                result.setErrors((List<Result.Error>) data);
            }
        }
        return result;
    }

    /**
     * 针对参数校验失败异常的处理
     *
     * @param exception 参数校验异常
     * @param request   http request
     * @param response  http response
     * @return 异常处理结果
     */
    @ExceptionHandler(value = {BindException.class, MethodArgumentNotValidException.class, ConstraintViolationException.class})
    public Result databindException(Exception exception, HttpServletRequest request, HttpServletResponse response) {
        log.error(MessageFormat.format("请求发生了非预期异常，出错的 url [{0}]，出错的描述为 [{1}]",
                request.getRequestURL().toString(), exception.getMessage()), exception);
        Result result = new Result();
        result.setDomain(domain);
        result.setCode(EnumExceptionMessageWebMvc.PARAM_VALIDATED_UN_PASS.getCode());
        result.setMsg(EnumExceptionMessageWebMvc.PARAM_VALIDATED_UN_PASS.getMessage());

        if (exception instanceof BindException) {
            for (FieldError fieldError : ((BindException) exception).getBindingResult().getFieldErrors()) {
                result.addError(fieldError.getField(), fieldError.getDefaultMessage());
            }
        } else if (exception instanceof MethodArgumentNotValidException) {
            for (FieldError fieldError : ((MethodArgumentNotValidException) exception).getBindingResult().getFieldErrors()) {
                result.addError(fieldError.getField(), fieldError.getDefaultMessage());
            }
        } else if (exception instanceof ConstraintViolationException) {
            for (ConstraintViolation cv : ((ConstraintViolationException) exception).getConstraintViolations()) {
                result.addError(cv.getPropertyPath().toString(), cv.getMessage());
            }
        }
        return result;
    }

    /**
     * 针对spring web 中的异常的处理
     *
     * @param exception Spring Web 异常
     * @param request   http request
     * @param response  http response
     * @return 异常处理结果
     */
    @ExceptionHandler(value = {
            NoHandlerFoundException.class,
            HttpRequestMethodNotSupportedException.class,
            HttpMediaTypeNotSupportedException.class
    })
    public Result springWebExceptionHandler(Exception exception, HttpServletRequest request, HttpServletResponse response) {
        log.error(MessageFormat.format("请求发生了非预期异常，出错的 url [{0}]，出错的描述为 [{1}]",
                request.getRequestURL().toString(), exception.getMessage()), exception);
        Result result = new Result();
        result.setDomain(domain);
        if (exception instanceof NoHandlerFoundException) {
            result.setCode(EnumExceptionMessageWebMvc.NO_HANDLER_FOUND_ERROR.getCode());
            result.setMsg(EnumExceptionMessageWebMvc.NO_HANDLER_FOUND_ERROR.getMessage());
        } else if (exception instanceof HttpRequestMethodNotSupportedException) {
            result.setCode(EnumExceptionMessageWebMvc.HTTP_REQUEST_METHOD_NOT_SUPPORTED_ERROR.getCode());
            result.setMsg(EnumExceptionMessageWebMvc.HTTP_REQUEST_METHOD_NOT_SUPPORTED_ERROR.getMessage());
        } else if (exception instanceof HttpMediaTypeNotSupportedException) {
            result.setCode(EnumExceptionMessageWebMvc.HTTP_MEDIA_TYPE_NOT_SUPPORTED_ERROR.getCode());
            result.setMsg(EnumExceptionMessageWebMvc.HTTP_MEDIA_TYPE_NOT_SUPPORTED_ERROR.getMessage());
        } else {
            result.setCode(EnumExceptionMessageWebMvc.UNEXPECTED_ERROR.getCode());
            result.setMsg(EnumExceptionMessageWebMvc.UNEXPECTED_ERROR.getMessage());
        }
        return result;
    }

    /**
     * 针对全局异常的处理
     *
     * @param exception 全局异常
     * @param request   http request
     * @param response  http response
     * @return 异常处理结果
     */
    @ExceptionHandler(value = Throwable.class)
    public Result throwableHandler(Exception exception, HttpServletRequest request, HttpServletResponse response) {
        log.error(MessageFormat.format("请求发生了非预期异常，出错的 url [{0}]，出错的描述为 [{1}]",
                request.getRequestURL().toString(), exception.getMessage()), exception);
        Result result = new Result();
        result.setDomain(domain);
        result.setCode(EnumExceptionMessageWebMvc.UNEXPECTED_ERROR.getCode());
        result.setMsg(EnumExceptionMessageWebMvc.UNEXPECTED_ERROR.getMessage());
        return result;
    }

}