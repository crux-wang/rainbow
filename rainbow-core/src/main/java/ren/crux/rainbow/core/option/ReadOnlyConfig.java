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

/**
 * 只读配置
 * <p>
 * 可修改的
 *
 * @author wangzhihui
 */
public interface ReadOnlyConfig {

    ReadOnlyConfig INSTANCE = new ReadOnlyConfig() {
    };

    /**
     * 获取配置的值
     *
     * @param option 配置项
     * @return 当前配置的值，如果没有配置则返回配置项的默认值，可能是 {@code null}
     */
    default <T> T getOption(@NonNull Option<T> option) {
        return option.getDefaultValue();
    }

    /**
     * 是否含有某配置项
     *
     * @param option 配置项
     * @param <T>
     * @return
     */
    default <T> boolean hasOption(@NonNull Option<T> option) {
        return false;
    }

    /**
     * 是否为空
     *
     * @return
     */
    default boolean isEmpty() {
        return true;
    }
}
