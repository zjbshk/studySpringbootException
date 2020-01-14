package cn.infomany.common.advice;

import cn.infomany.common.exception.BusinessException;
import cn.infomany.common.response.AjaxResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.util.Set;


/**
 * @author Zjb
 * @description:全局异常处理
 */
@ControllerAdvice
@ResponseBody
public class CommonExceptionAdvice {

    private static Logger logger = LoggerFactory.getLogger(CommonExceptionAdvice.class);

    /**
     * 参数缺失，无法获取具体的信息
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public AjaxResult handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        logger.error("缺少请求参数");
        return new AjaxResult(HttpStatus.BAD_REQUEST.value(), e.getMessage());
    }

    /**
     * 参数解析失败
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public AjaxResult handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        logger.error("参数解析失败");
        return new AjaxResult(HttpStatus.BAD_REQUEST.value(), e.getMessage());
    }

    /**
     * 参数验证失败
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public AjaxResult handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        logger.error("参数验证失败");
        BindingResult result = e.getBindingResult();
        FieldError error = result.getFieldError();
        String field = error.getField();
        String code = error.getDefaultMessage();
        String message = String.format("%s:%s", field, code);
        return new AjaxResult(HttpStatus.BAD_REQUEST.value(), message);
    }

    /**
     * 参数绑定失败
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BindException.class)
    public AjaxResult handleBindException(BindException e) {
        logger.error("参数绑定失败");
        BindingResult result = e.getBindingResult();
        FieldError error = result.getFieldError();
        String field = error.getField();
        String code = error.getDefaultMessage();
        String message = String.format("%s:%s", field, code);
        return new AjaxResult(HttpStatus.BAD_REQUEST.value(), message);
    }

    /**
     * 参数验证失败
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public AjaxResult handleServiceException(ConstraintViolationException e) {
        logger.error("参数验证失败");
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        ConstraintViolation<?> violation = violations.iterator().next();
        String message = violation.getMessage();
        return new AjaxResult(HttpStatus.BAD_REQUEST.value(), message);
    }

    /**
     * 参数验证失败
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ValidationException.class)
    public AjaxResult handleValidationException(ValidationException e) {
        logger.error("参数验证失败");
        return new AjaxResult(HttpStatus.BAD_REQUEST.value(), e.getMessage());
    }

    /**
     * 不支持当前请求方法
     */
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public AjaxResult handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        logger.error("不支持当前请求方法");
        return new AjaxResult(HttpStatus.METHOD_NOT_ALLOWED.value(), e.getMessage());
    }

    /**
     * 不支持当前媒体类型
     */
    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public AjaxResult handleHttpMediaTypeNotSupportedException(Exception e) {
        logger.error("不支持当前媒体类型");
        return new AjaxResult(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value(), e.getMessage());
    }

    /**
     * 业务逻辑异常
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(BusinessException.class)
    public AjaxResult handleServiceException(BusinessException e) {
        logger.error("业务逻辑异常");
        return new AjaxResult(e.getCode(), e.getMessage());
    }


    /**
     * 操作数据库出现异常:名称重复，外键关联
     */
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//    @ExceptionHandler(DataIntegrityViolationException.class)
//    public AjaxResult handleException(DataIntegrityViolationException e) {
//        logger.error("操作数据库出现异常:");
//        return new AjaxResult(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
//    }

    /**
     * 通用异常,最后一道防御
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public AjaxResult handleException(Exception e) {
        logger.error("通用异常");
        return new AjaxResult(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
    }
}