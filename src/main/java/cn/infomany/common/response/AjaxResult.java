package cn.infomany.common.response;

import cn.infomany.common.exception.BusinessException;

/**
 * @description: 这是一个通用返回
 * @author: zhanjinbing
 * @data: 2020-01-14 17:22
 */
public class AjaxResult {

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


}
