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

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * 配置项
 *
 * @author wangzhihui
 */
@Getter
@Setter
@EqualsAndHashCode(of = "name")
public class Option<T> {

    private final String name;
    private final T defaultValue;
    private Class<T> type;

    private Option(String name, T defaultValue, Class<T> type) {
        this.name = name;
        this.defaultValue = defaultValue;
        this.type = type;
    }

    @SuppressWarnings("unchecked")
    public static <T> Option<T> valueOf(String name, T defaultValue) {
        return new Option<>(name, defaultValue, defaultValue == null ? null : (Class<T>) defaultValue.getClass());
    }

    public static <T> Option<T> valueOf(String name) {
        return new Option<>(name, null, null);
    }

    public static <T> Option<T> valueOf(String name, Class<T> type) {
        return new Option<>(name, null, type);
    }

    @Override
    public String toString() {
        return name + "(" + (type == null ? "Unknown" : type.getSimpleName()) + ":" + defaultValue + ")";
    }
}
