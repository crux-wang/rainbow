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
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;

/**
 * {@link DynamicConfig} 默认实现
 * <p>
 * 与 {@link DefaultConfig} 相比只是将 {@code Value} 设置成 {@link DelegateCallable} 类型
 * 在调用 {@link ReadOnlyConfig#getOption(Option)} 时检查 {@code Value} 类型
 * 如果是 {@link DelegateCallable} 类型，即执行 {@link DelegateCallable#call()} 方法并返回结果
 * 当执行 {@link DelegateCallable#call()} 发生异常时返回 {@link Option#getDefaultValue()}
 *
 * @author wangzhihui
 */
@Slf4j
public class DefaultDynamicConfig extends DefaultConfig implements DynamicConfig {

    @Override
    public ReadOnlyConfig build() {
        return new DynamicReadOnlyConfig(options);
    }

    @Override
    public <T> void setOptionCallable(@NonNull Option<T> option, @NonNull Callable<T> callable) {
        options.put(option, new DelegateCallable<>(callable));
    }

    /**
     * 防止外部使用
     *
     * @param <T>
     */
    final class DelegateCallable<T> implements Callable<T> {
        private final Callable<T> source;

        private DelegateCallable(@NonNull Callable<T> source) {
            this.source = source;
        }

        @Override
        public T call() throws Exception {
            return source.call();
        }
    }
}
