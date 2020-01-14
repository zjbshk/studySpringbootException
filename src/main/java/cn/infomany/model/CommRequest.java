package cn.infomany.model;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;
import java.util.Date;

public class CommRequest {

    @NotEmpty(message = "[name]不能为空")
    private String name;

    @NotNull(message = "[age]不能为空")
    @Length(max = 100, message = "[age]不能超过100")
    private Integer age;

    @Length(max = 1000, message = "[height]不能超过1000")
    private int height;

    @PositiveOrZero(message = "[width]必须为正数或零")
    private long width;

    @AssertTrue(message = "[sex]必须要是正数")
    private boolean sex;

    @Digits(integer = 2, fraction = 5, message = "[digit]不符合固定")
    private float digit;

    @Email(message = "[email]格式有误")
    private String myEmail;

    @PastOrPresent(message = "[birth]只能是过去的时间")
    private Date birth;

}
