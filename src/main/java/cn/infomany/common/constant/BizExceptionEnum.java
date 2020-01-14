package cn.infomany.common.constant;

public enum BizExceptionEnum {

    ;

    // 异常值
    private int code;

    // 中文异常信息
    private String chMsg;

    // 英文异常信息
    private String enMsg;


    BizExceptionEnum(int code, String chMsg, String enMsg) {
        this.code = code;
        this.chMsg = chMsg;
        this.enMsg = enMsg;
    }
}
