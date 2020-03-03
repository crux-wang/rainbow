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

package ren.crux.rainbow.test.demo.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import ren.crux.rainbow.test.demo.model.User;

/**
 * 用户模块
 */
@RestController
@RequestMapping("/user")
public class UserController {
    /**
     * 创建用户
     *
     * @param user 用户资料
     * @return 用户资料
     */
    @PostMapping
    public User create(@RequestBody User user) {
        return user;
    }

    /**
     * 更新用户资料
     *
     * @param id   用户 ID
     * @param user 用户资料
     * @return 用户资料
     * @throws Exception 异常
     */
    @PostMapping("/{id}")
    public User update(@PathVariable String id, @RequestBody User user) throws Exception {
        user.setId(id);
        return user;
    }

    /**
     * 删除用户
     *
     * @param id 用户 ID
     */
    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
    }

    /**
     * 用户列表
     *
     * @param pageable 分页信息
     * @return 用户列表
     */
    @GetMapping
    public Page<User> users(@PageableDefault Pageable pageable) {
        return Page.empty(pageable);
    }

}
