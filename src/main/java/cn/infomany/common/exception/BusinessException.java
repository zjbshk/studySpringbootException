package cn.infomany.common.exception;

import cn.infomany.common.constant.BizExceptionEnum;

/**
 * @author Zjb
 */
public class BusinessException extends RuntimeException {

    private int code;

    public BusinessException(String msg) {
        super(msg);
    }

    public BusinessException(int code, String msg) {
        this(msg);
        this.code = code;
    }

    public BusinessException(BizExceptionEnum exceptionEnum) {
        this(exceptionEnum.getChMsg());
        this.code = exceptionEnum.getCode();
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }


}