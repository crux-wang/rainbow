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

import java.util.Collections;

/**
 * 只有一个常量配置项的 {@link Config}
 * <p>
 * {@link #value} 不支持配置任何项，当调用 {@link #setOption(Option, Object)} 时 抛出 {@link UnsupportedOperationException}
 * 当获取未设置的配置时返回 {@link Option#getDefaultValue()}
 *
 * @author wangzhihui
 */
public class SingletonConstantConfig implements Config {

    private final Option<?> option;
    private final Object value;

    private SingletonConstantConfig(Option<?> option, Object value) {
        this.option = option;
        this.value = value;
    }

    public static Config of(Option<?> option) {
        return new SingletonConstantConfig(option, option.getDefaultValue());
    }

    public static Config of(Option<?> option, Object value) {
        return new SingletonConstantConfig(option, value);
    }

    @Override
    public <T> SingletonConstantConfig setOption(Option<T> option, T value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ReadOnlyConfig build() {
        return new DefaultReadOnlyConfig(Collections.singletonMap(option, value));
    }
}
