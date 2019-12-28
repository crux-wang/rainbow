/*
 * Copyright 2019 The Crux Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ren.crux.rainbow.core.option;

/**
 * 一组配置属性
 * <p>
 * 用于对外暴露自身可配置项
 *
 * @author wangzhihui
 */
public interface Config {

    /**
     * 设置配置项的值
     *
     * @param option 配置项
     * @param value  给定配置项的值 （ 可以为 {@code null} ）
     * @return 当且仅当设置成功时返回 {@code true}
     */
    <T> Config setOption(Option<T> option, T value);

    /**
     * 生成只读配置
     *
     * @return 当前配置的只读版本
     */
    ReadOnlyConfig build();

}
