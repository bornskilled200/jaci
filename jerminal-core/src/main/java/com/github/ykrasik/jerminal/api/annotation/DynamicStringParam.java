/******************************************************************************
 * Copyright (C) 2014 Yevgeny Krasik                                          *
 *                                                                            *
 * Licensed under the Apache License, Version 2.0 (the "License");            *
 * you may not use this file except in compliance with the License.           *
 * You may obtain a copy of the License at                                    *
 *                                                                            *
 * http://www.apache.org/licenses/LICENSE-2.0                                 *
 *                                                                            *
 * Unless required by applicable law or agreed to in writing, software        *
 * distributed under the License is distributed on an "AS IS" BASIS,          *
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.   *
 * See the License for the specific language governing permissions and        *
 * limitations under the License.                                             *
 ******************************************************************************/

package com.github.ykrasik.jerminal.api.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that this parameter is a dynamic string parameter.<br>
 * Dynamic string parameters have their range of possible values computed at runtime (as opposed to
 * static strings which have their possible values pre-computed).<br>
 * Optional, any parameter not annotated will be considered a mandatory parameter that accepts
 * all strings, and will have a name and description generated for it.
 * However, if a bit more control over the parameter is required, annotate it.
 *
 * @author Yevgeny Krasik
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface DynamicStringParam {
    /**
     * @return Parameter name. If empty, a default name will be generated.
     */
    String value() default "";

    /**
     * @return Parameter description. If empty, a default description will be generated.
     */
    String description() default "";

    /**
     * @return True if this parameter is optional.
     *         Optional parameters will use the value returned by {@link #defaultValue()} if they weren't bound.
     */
    boolean optional() default false;

    /**
     * @return The default value acceptable by this parameter if a value isn't explicitly passed.
     *         Only used if {@link #optional()} is true.
     */
    String defaultValue() default "";

    /**
     * @return The name of a method that takes no args and returns an array of {@link String} defined in the same object.
     *         This method will be invoked in runtime to determine the possible values for this parameter.
     *         If the supplier returns an empty list, all values will be permitted.
     */
    String supplier();
}
