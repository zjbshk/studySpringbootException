package cn.infomany.controller;

import java.util.List;

import cn.infomany.common.exception.BusinessException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @description:全局异常处理演示入口
 */
@RestController
public class AdviceController {

  @RequestMapping("/hello1")
  public String hello1() {
    int i = 1 / 0;
    return "hello";
  }

  @RequestMapping("/hello2")
  public String hello2(Long id) {
    String string = null;
    string.length();
    return "hello";
  }

  @RequestMapping("/hello3")
  public List<String> hello3() {
    throw new BusinessException("test");
  }
}