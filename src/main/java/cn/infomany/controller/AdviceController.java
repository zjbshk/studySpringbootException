package cn.infomany.controller;

import cn.infomany.common.constant.BizExceptionEnum;
import cn.infomany.common.exception.BusinessException;
import cn.infomany.common.response.AjaxResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;


/**
 * @description: 全局异常处理演示入口
 */
@RestController
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
        throw new BusinessException(BizExceptionEnum.METHOD_NOT_FOUND);
    }


    @GetMapping("/hello")
    public String hello() {
        return "HelloWorld";
    }

    @GetMapping("/hello1")
    public AjaxResult hello1() {
        return new AjaxResult(11, "helload", new ArrayList<String>() {
            {
                add("zjb");
                add("ksfldsf");
            }
        });
    }
}