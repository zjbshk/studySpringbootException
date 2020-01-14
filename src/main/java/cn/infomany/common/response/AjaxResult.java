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
