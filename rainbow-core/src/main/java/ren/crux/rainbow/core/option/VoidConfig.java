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
 * 只返回配置项的默认值，用于初始值
 *
 * @author wangzhihui
 */
public class VoidConfig implements Config {

    public static Config INSTANCE = new VoidConfig();

    @Override
    public <T> VoidConfig setOption(Option<T> option, T value) {
        return this;
    }

    @Override
    public ReadOnlyConfig build() {
        return ReadOnlyConfig.INSTANCE;
    }
}
