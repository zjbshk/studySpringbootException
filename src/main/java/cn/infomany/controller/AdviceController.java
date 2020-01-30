package cn.infomany.controller;

import cn.infomany.common.constant.BizExceptionEnum;
import cn.infomany.common.exception.BusinessException;
import cn.infomany.common.response.AjaxResult;
import cn.infomany.model.CommRequest;
import cn.infomany.validator.IdentityCardNumber;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.List;


/**
 * @author DELL
 * @description: 全局异常处理演示入口
 */
@RestController
@Validated
public class AdviceController {

    /**
     * 数学运算异常
     */
    @GetMapping("/mathematicalOperationException")
    public String mathematicalOperationException() {
        int zero = 0;
        int i = 1 / zero;
        return "HelloWorld";
    }

    /**
     * 空指针异常
     */
    @GetMapping("/nullPointerException")
    public String nullPointerException() {
        String string = null;
        string.length();
        return "HelloWorld";
    }

    /**
     * 测试业务异常
     */
    @GetMapping("/businessException")
    public List<String> businessException() {
        throw new BusinessException(BizExceptionEnum.USER_NOT_FOUND);
    }


    @PostMapping("/objParameterCheck")
    public String objParameterCheck(@Valid @RequestBody CommRequest commRequest) {
        return "HelloWorld";
    }

    @PostMapping("/missingType")
    public String missingType(@RequestHeader String cookie) {
        return cookie;
    }


    @PostMapping("/directInspection")
    public String directInspection(@RequestParam String cookie, @RequestHeader String token) {
        return cookie + token;
    }


    /**
     * 像这种直接在参数前面用检验注解，该类上必须要有Validated注解，才能生效
     */
    @PostMapping("/parameterCheck")
    public String parameterCheck(@Pattern(regexp = "\\d+", message = "不匹配") @RequestParam String cookie) {
        return cookie;
    }

    /**
     * 像这种直接在参数前面用检验注解，该类上必须要有Validated注解，才能生效
     */
    @PostMapping("/parameterCheck1")
    public String parameterCheck1(@NotBlank(message = "[cookie]不能为空") @Pattern(regexp = "\\d+", message = "不匹配") @RequestParam(required = false) String cookie) {
        return cookie;
    }

    /**
     * 用法是：路径变量:正则表达式。
     * 当请求URI不满足正则表达式时，
     * 客户端将收到404错误码。
     * 不方便的地方是，不能通过捕获异常的方式，向前端返回统一的、自定义格式的响应参数
     */
    @GetMapping("/path/{id:[0-9]{3,4}}")
    public String testPathVai(@PathVariable String id) {
        return id;
    }

    /**
     * 自定义参数检验
     * @param idCard
     * @return
     */
    @GetMapping("/idCard")
    public String testIdCard(@IdentityCardNumber @RequestParam String idCard) {
        return idCard;
    }

}