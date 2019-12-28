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

import lombok.NonNull;

import java.util.concurrent.Callable;

/**
 * 在 {@link Config} 的基础上支持动态获取配置
 *
 * @author wangzhihui
 */
public interface DynamicConfig extends Config {

    /**
     * 设置获取配置方法
     *
     * @param option   配置项
     * @param callable 获取配置的方法
     * @param <T>      配置值类型
     */
    <T> void setOptionCallable(@NonNull Option<T> option, @NonNull Callable<T> callable);

}
