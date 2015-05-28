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

package com.github.ykrasik.jemi.cli.command;

import com.github.ykrasik.jemi.cli.directory.CliDirectory;
import com.github.ykrasik.jemi.core.command.CommandArgsImpl;
import lombok.NonNull;

import java.util.List;
import java.util.Map;

/**
 * @author Yevgeny Krasik
 */
// TODO: JavaDoc
public class CliCommandArgsImpl extends CommandArgsImpl implements CliCommandArgs {
    public CliCommandArgsImpl(@NonNull List<Object> positionalArgs, @NonNull Map<String, Object> namedArgs) {
        super(positionalArgs, namedArgs);
    }

    @Override
    public CliDirectory getDirectory(String name) throws IllegalArgumentException {
        return getArg(name, CliDirectory.class);
    }

    @Override
    public CliDirectory popDirectory() throws IllegalArgumentException {
        return popArg(CliDirectory.class);
    }

    @Override
    public CliCommand getCommand(String name) throws IllegalArgumentException {
        return getArg(name, CliCommand.class);
    }

    @Override
    public CliCommand popCommand() throws IllegalArgumentException {
        return popArg(CliCommand.class);
    }
}
