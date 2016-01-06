/******************************************************************************
 * Copyright (C) 2015 Yevgeny Krasik                                          *
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

package com.github.ykrasik.jaci.command.toggle;

import com.github.ykrasik.jaci.api.ToggleCommandStateAccessor;
import com.github.ykrasik.jaci.util.function.Spplr;


/**
 * A {@link Spplr} that returns the inverse of the current {@link ToggleCommandStateAccessor#get()}.
 */
public class ToggleCommandAccessorDefaultValueSupplier implements Spplr<Boolean> {
    private final ToggleCommandStateAccessor accessor;

    public ToggleCommandAccessorDefaultValueSupplier( ToggleCommandStateAccessor accessor) {
        this.accessor = accessor;
    }

    @Override
    public Boolean get() {
        return !accessor.get();
    }
}
