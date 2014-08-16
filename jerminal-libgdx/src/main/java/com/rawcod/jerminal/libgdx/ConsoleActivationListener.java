/*
 * Copyright (C) 2014 Yevgeny Krasik
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.rawcod.jerminal.libgdx;

/**
 * A callback for console activation/deactivation.
 *
 * @author Yevgeny Krasik
 */
public interface ConsoleActivationListener {
    /**
     * Called when the console is activated.
     */
    void activated();

    /**
     * Called when the console is deactivated.
     */
    void deactivated();
}
