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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wangzhihui
 */
public class DefaultReadOnlyConfig implements ReadOnlyConfig {

    protected final Map<Option<?>, Object> options;

    public DefaultReadOnlyConfig(Map<Option<?>, Object> options) {
        this.options = options == null ? Collections.emptyMap() : Collections.unmodifiableMap(new HashMap<>(options));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getOption(@NonNull Option<T> option) {
        return (T) options.getOrDefault(option, option.getDefaultValue());
    }

    @Override
    public <T> boolean hasOption(@NonNull Option<T> option) {
        return options.containsKey(option);
    }

    @Override
    public boolean isEmpty() {
        return options.isEmpty();
    }
}
