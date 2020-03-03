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

package ren.crux.rainbow.swagger2;

import springfox.documentation.swagger.common.SwaggerPluginSupport;

/**
 * RainbowSwaggerPluginSupport
 *
 * @author wangzhihui
 **/
public class RainbowSwaggerPluginSupport {

    public final static int RAINBOW_SWAGGER_PLUGIN_ORDER = SwaggerPluginSupport.SWAGGER_PLUGIN_ORDER - 10;

}
