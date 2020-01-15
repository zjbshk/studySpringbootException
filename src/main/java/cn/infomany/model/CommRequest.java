package cn.infomany.model;


import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.util.Date;

/**
 * @author DELL
 */
@Data
public class CommRequest {

    @NotEmpty(message = "不能为空")
    private String name;

    @NotNull(message = "不能为空")
    @Max(100)
    private Integer age;

    @PositiveOrZero(message = "必须为正数或零")
    @Max(15)
    private int height;

    @PositiveOrZero(message = "必须为正数或零")
    @Max(20)
    private long width;

    @AssertTrue(message = "必须要是正数")
    private boolean sex;

    @Digits(integer = 2, fraction = 1, message = "不符合固定")
    private float digit;

    @Email(message = "格式有误")
    private String myEmail;

    @PastOrPresent(message = "只能是过去的时间")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date birth;

}
