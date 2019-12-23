package ren.crux.rainbow.test.demo.model;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;
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

    /**
     * 是否设置了昵称
     *
     * @return 是否设置了昵称
     */
    public boolean isSetNickname() {
        return nickname != null;
    }

    /**
     * 获取邮件前缀
     *
     * @return 邮件前缀
     * @throws Exception 异常
     * @see #email
     */
    public String getEmailPrefix() throws Exception {
        return StringUtils.substringBefore(email, "@");
    }

}
