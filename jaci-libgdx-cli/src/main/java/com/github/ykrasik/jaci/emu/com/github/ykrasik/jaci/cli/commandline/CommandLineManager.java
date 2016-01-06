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

package com.github.ykrasik.jaci.cli.commandline;

/**
 * A component that can communicate (read/write) with the command-line.
 * It is assumed that the command-line does not change itself in any matter, only through this component.
 * Command line is expected to keep the current command-line text and caret position as state.
 *
 * @author Yevgeny Krasik
 */
public interface CommandLineManager {
    /**
     * @return The current command line.
     */
    String getCommandLine();

    /**
     * Set the command line.
     *
     * @param commandLine Command line to set.
     */
    void setCommandLine(String commandLine);

    /**
     * @return The current caret position.
     */
    int getCaret();

    /**
     * Set the caret to the specified position.
     *
     * @param position Position to set the caret to.
     */
    void setCaret(int position);
}
