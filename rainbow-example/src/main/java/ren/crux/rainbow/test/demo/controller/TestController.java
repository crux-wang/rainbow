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

import org.springframework.web.bind.annotation.*;
import ren.crux.rainbow.test.demo.model.SubTest;
import ren.crux.rainbow.test.demo.model.Test;

import java.util.List;

/**
 * 测试专用
 * <p>
 * {@link Test}
 *
 * @author wangzhihui
 */
@RestController
@RequestMapping("/test")
public class TestController {
    /**
     * 测试
     *
     * @param body    Request Body
     * @param param   Request Param
     * @param attr    Request Attribute
     * @param pv      Path Variable
     * @param cookie  Cookie Value
     * @param session Session Attribute
     * @return 测试实体 {@link Test}
     */
    @RequestMapping(value = {"/get", "/get-2"}, method = {RequestMethod.GET, RequestMethod.POST})
    public Test test(
            @RequestBody Test body,
            @RequestParam(defaultValue = "xxx") String param,
            @RequestAttribute String attr,
            @PathVariable(required = false) String pv,
            @CookieValue(name = "ckv") String cookie,
            @SessionAttribute String session) {
        return null;
    }

    @GetMapping("/subtest")
    public SubTest subtest(List<String> list) {
        return null;
    }
}
