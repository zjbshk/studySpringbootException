package cn.infomany.common.exception;

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


}