/*
 *  Copyright 2020. The Crux Authors
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package ren.crux.rainbow.test.demo.model;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

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
    @Size(min = 2, max = 10)
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
