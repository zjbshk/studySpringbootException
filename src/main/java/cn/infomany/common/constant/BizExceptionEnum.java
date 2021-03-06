package cn.infomany.common.constant;

/**
 * @author Zjb
 */

public enum BizExceptionEnum {

    /**
     * 枚举异常类型
     */
    METHOD_NOT_FOUND(1, "方法没有找到", "method not found"),
    USER_NOT_FOUND(2, "用户没有找到", "user not found");

    /**
     * 异常值
     */
    private int code;

    /**
     * 中文异常信息
     */
    private String chMsg;

    /**
     * 英文异常信息
     */
    private String enMsg;


    BizExceptionEnum(int code, String chMsg, String enMsg) {
        this.code = code;
        this.chMsg = chMsg;
        this.enMsg = enMsg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getChMsg() {
        return chMsg;
    }

    public void setChMsg(String chMsg) {
        this.chMsg = chMsg;
    }

    public String getEnMsg() {
        return enMsg;
    }

    public void setEnMsg(String enMsg) {
        this.enMsg = enMsg;
    }
}
