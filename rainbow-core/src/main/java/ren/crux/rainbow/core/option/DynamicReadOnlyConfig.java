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

import java.util.Map;

/**
 * @author wangzhihui
 */
@Slf4j
public class DynamicReadOnlyConfig extends DefaultReadOnlyConfig {

    public DynamicReadOnlyConfig(Map<Option<?>, Object> options) {
        super(options);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getOption(@NonNull Option<T> option) {
        Object o = options.get(option);
        if (o == null) {
            return option.getDefaultValue();
        }
        if (o instanceof DefaultDynamicConfig.DelegateCallable) {
            try {
                return (T) ((DefaultDynamicConfig.DelegateCallable) o).call();
            } catch (Exception e) {
                log.error("get option ( {} ) err! use default value.", option.getName(), e);
                return option.getDefaultValue();
            }
        }
        return (T) o;
    }
}
