> 用`springboot`开发项目时，我们需要处理各式各样的异常。对于前后端分离的项目，对异常的处理更为重要，我们需要将异常封装成固定的格式，方便前端处理。

> 对于接口不存在异常，接口请求的方法不对等，springboot是有自己处理，但是格式可能不太符合我们的要求

### 下面就开始介绍如何管理springboot的全局异常
项目的搭建：
##### `maven`项目的`pom.xml`:
```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>cn.infomany</groupId>
    <artifactId>studySpringbootException</artifactId>
    <version>1.0-SNAPSHOT</version>

    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.1.9.RELEASE</version>
        <relativePath/>
    </parent>

    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <java.version>1.8</java.version>
    </properties>

    <dependencies>

        <!-- web -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <!-- 持久层 -->
<!--        <dependency>-->
<!--            <groupId>org.springframework.boot</groupId>-->
<!--            <artifactId>spring-boot-starter-data-jpa</artifactId>-->
<!--        </dependency>-->


        <!-- 测试 -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <!-- 只在test测试里面运行 -->
            <scope>test</scope>
        </dependency>

        <!--    java开发的工具包，使得类更加简洁    -->
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>

    </dependencies>

    <build>
        <finalName>spring-boot-controller-advice</finalName>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <configuration>
                    <source>${java.version}</source>
                    <target>${java.version}</target>
                </configuration>
            </plugin>
        </plugins>
    </build>

</project>
```
#####  编写通用的请求返回信息格式：
```java
package cn.infomany.common.response;

import cn.infomany.common.exception.BusinessException;

import java.io.Serializable;

/**
 * @description: 这是一个通用返回
 * @author: zhanjinbing
 * @data: 2020-01-14 17:22
 */
public class AjaxResult implements Serializable {

    private int code;
    private String msg;
    private Object data;


    public AjaxResult(int code, Object data) {
        this.code = code;
        this.data = data;
    }

    public AjaxResult(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public AjaxResult(BusinessException ex) {
        this.msg = ex.getMessage();
    }

    public AjaxResult(int code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
```
##### 全局异常处理类
```java
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
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.util.HashMap;
import java.util.Map;
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
        logger.error("请求缺少参数");

        String msg = String.format("请求缺少参数[%s]", e.getParameterName());
        return new AjaxResult(HttpStatus.BAD_REQUEST.value(), msg);
    }

    /**
     * 参数解析失败
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public AjaxResult handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        logger.error("参数解析失败");

        String msg = e.getCause() == null ? e.getMessage() : e.getCause().getMessage();
        Map<String, String> map = new HashMap<String, String>() {
            {
                put("Required request body is missing", "Required request body is missing");
            }
        };

        for (Map.Entry<String, String> kv : map.entrySet()) {
            String key = kv.getKey();
            if (e.getMessage() != null && e.getMessage().contains(key)) {
                msg = kv.getValue();
            }
        }

        return new AjaxResult(HttpStatus.BAD_REQUEST.value(), msg);
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
        String message = String.format("[%s]:%s", field, code);
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
        String msg = String.format("该接口不支持%s方法,支持%s方法", e.getMethod(), e.getSupportedHttpMethods());
        return new AjaxResult(HttpStatus.METHOD_NOT_ALLOWED.value(), msg);
    }

    /**
     * 不支持当前媒体类型
     */
    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public AjaxResult handleHttpMediaTypeNotSupportedException(Exception e) {
        logger.error("不支持当前媒体类型{}", e.getMessage());
        return new AjaxResult(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value(), e.getMessage());
    }

    /**
     * 业务逻辑异常
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(BusinessException.class)
    public AjaxResult handleServiceException(BusinessException e) {
        logger.error("业务逻辑异常:{}", e.getMessage());
        return new AjaxResult(e.getCode(), e.getMessage());
    }

    /**
     * 头丢失异常
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingRequestHeaderException.class)
    public AjaxResult missingRequestHeaderException(MissingRequestHeaderException e) {
        logger.error("头丢失异常:{}", e.getMessage());

        String msg = String.format("请求头部缺少字段[%s]", e.getHeaderName());
        return new AjaxResult(HttpStatus.BAD_REQUEST.value(), msg);
    }


    /**
     * 通用异常,最后一道防御
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public AjaxResult handleException(Exception e) {
        logger.error("通用异常:{}", e.getMessage());
        return new AjaxResult(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
    }
}
```
##### 编写接管`springboot`有自定义的异常（`404`等）
```
package cn.infomany.common.advice;

import cn.infomany.common.response.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author DELL
 * @Description: Springboot全局异常统一处理
 */
@RestController
@EnableConfigurationProperties({ServerProperties.class})
public class ExceptionController implements ErrorController {

    private ErrorAttributes errorAttributes;

    @Autowired
    private ServerProperties serverProperties;


    /**
     * 初始化ExceptionController
     *
     * @param errorAttributes
     */
    @Autowired
    public ExceptionController(ErrorAttributes errorAttributes) {
        Assert.notNull(errorAttributes, "ErrorAttributes must not be null");
        this.errorAttributes = errorAttributes;
    }


    @RequestMapping(value = "/error")
    @ResponseBody
    public AjaxResult error(HttpServletRequest request) {
        Map<String, Object> body = getErrorAttributes(request,
                isIncludeStackTrace(request, MediaType.ALL));
        HttpStatus status = getStatus(request);
        String tag = "message";
        String msg = body.get(tag).toString();

        if (HttpStatus.NOT_FOUND.equals(status)) {
            msg = String.format("[%s]该接口不存在,请检查", body.get("path"));
        }

        return new AjaxResult(status.value(), msg);
    }


    /**
     * Determine if the stacktrace attribute should be included.
     *
     * @param request  the source request
     * @param produces the media type produced (or {@code MediaType.ALL})
     * @return if the stacktrace attribute should be included
     */
    protected boolean isIncludeStackTrace(HttpServletRequest request,
                                          MediaType produces) {
        ErrorProperties.IncludeStacktrace include = this.serverProperties.getError().getIncludeStacktrace();
        if (include == ErrorProperties.IncludeStacktrace.ALWAYS) {
            return true;
        }
        if (include == ErrorProperties.IncludeStacktrace.ON_TRACE_PARAM) {
            return getTraceParameter(request);
        }
        return false;
    }


    /**
     * 获取错误的信息
     *
     * @param request
     * @param includeStackTrace
     * @return
     */
    private Map<String, Object> getErrorAttributes(HttpServletRequest request,
                                                   boolean includeStackTrace) {
        ServletWebRequest servletWebRequest = new ServletWebRequest(request);
        return this.errorAttributes.getErrorAttributes(servletWebRequest,
                includeStackTrace);
    }

    /**
     * 是否包含trace
     *
     * @param request
     * @return
     */
    private boolean getTraceParameter(HttpServletRequest request) {
        String parameter = request.getParameter("trace");
        if (parameter == null) {
            return false;
        }
        return !"false".equals(parameter.toLowerCase());
    }

    /**
     * 获取错误编码
     *
     * @param request
     * @return
     */
    private HttpStatus getStatus(HttpServletRequest request) {
        Integer statusCode = (Integer) request
                .getAttribute("javax.servlet.error.status_code");
        if (statusCode == null) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
        try {
            return HttpStatus.valueOf(statusCode);
        } catch (Exception ex) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }

    /**
     * 实现错误路径,暂时无用
     *
     * @return
     */
    @Override
    public String getErrorPath() {
        return "";
    }

}
```

如果贴出全部代码，量太多了，这里就不再贴了，具体可以看下面的源码，源码中放了`postman`的请求接口文档，并添加示例
postman文件名:`studyjavaexception.postman_collection.json`

![](https://upload-images.jianshu.io/upload_images/7473008-9ccea6aad55bb6a4.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)



源码地址如下：
https://github.com/zjbshk/studySpringbootException.git