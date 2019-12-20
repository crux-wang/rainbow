package ren.crux.rainbow.test.demo.model;

import lombok.Data;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * 用户
 */
@Data
public class User {
    /**
     * 用户 ID
     */
    private String id;
    /**
     * 昵称
     */
    @NotNull
    @Max(10)
    @Min(2)
    private String nickname;
    /**
     * 头像
     */
    @URL
    private String avatar;
    /**
     * 邮箱
     */
    @Email
    private String email;

}
