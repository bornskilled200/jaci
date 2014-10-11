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

package com.github.ykrasik.jerminal.internal.filesystem.command;

import com.github.ykrasik.jerminal.ShellConstants;
import com.github.ykrasik.jerminal.api.assist.CommandInfo;
import com.github.ykrasik.jerminal.api.assist.ParamAndValue;
import com.github.ykrasik.jerminal.api.command.CommandArgs;
import com.github.ykrasik.jerminal.api.command.parameter.CommandParam;
import com.github.ykrasik.jerminal.api.filesystem.command.Command;
import com.github.ykrasik.jerminal.collections.trie.Trie;
import com.github.ykrasik.jerminal.collections.trie.TrieImpl;
import com.github.ykrasik.jerminal.internal.Describable;
import com.github.ykrasik.jerminal.internal.command.parameter.CommandParamManager;
import com.github.ykrasik.jerminal.internal.exception.ParseException;
import com.github.ykrasik.jerminal.internal.exception.ShellException;
import com.github.ykrasik.jerminal.internal.returnvalue.AssistReturnValue;
import com.github.ykrasik.jerminal.internal.returnvalue.AutoCompleteReturnValue;
import com.google.common.base.Optional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Yevgeny Krasik
 */
// FIXME: JavaDoc
public class InternalCommand implements Describable {
    private final Command command;
    private final Trie<CommandParam> paramsTrie;

    public InternalCommand(Command command) {
        this.command = Objects.requireNonNull(command);
        this.paramsTrie = createParamTrie(command.getParams());
    }

    private Trie<CommandParam> createParamTrie(List<CommandParam> params) {
        Trie<CommandParam> trie = new TrieImpl<>();
        for (CommandParam param : params) {
            final String name = param.getName();
            if (!ShellConstants.isValidName(name)) {
                throw new ShellException("Invalid name for parameter: '%s'", name);
            }

            trie = trie.add(name, param);
        }
        return trie;
    }

    @Override
    public String getName() {
        return command.getName();
    }

    @Override
    public String getDescription() {
        return command.getDescription();
    }

    /**
     * @return The command wrapped by this object.
     */
    public Command getCommand() {
        return command;
    }

    /**
     * @return Parsed args for the command.
     *
     * @throws ParseException If the one of the args is invalid or a mandatory parameter is missing.
     */
    public CommandArgs parseCommandArgs(List<String> args) throws ParseException {
        final CommandParamManager paramManager = new CommandParamManager(paramsTrie, command.getParams());
        try {
            return paramManager.parseCommandArgs(args);
        } catch (ParseException e) {
            // Add command info to the exception.
            final CommandInfo commandInfo = createCommandInfo(paramManager);
            throw e.withCommandInfo(commandInfo);
        }
    }

    /**
     * @return Assistance for the next available {@link CommandParam}.
     *
     * @throws ParseException If the one of the args is invalid or a mandatory parameter is missing.
     */
    public AssistReturnValue assistArgs(List<String> args) throws ParseException {
        final CommandParamManager paramManager = new CommandParamManager(paramsTrie, command.getParams());
        try {
            final AutoCompleteReturnValue autoCompleteReturnValue = paramManager.autoCompleteLastArg(args);
            final CommandInfo commandInfo = createCommandInfo(paramManager);
            return new AssistReturnValue(Optional.of(commandInfo), autoCompleteReturnValue);
        } catch (ParseException e) {
            // Add command info to the exception.
            final CommandInfo commandInfo = createCommandInfo(paramManager);
            throw e.withCommandInfo(commandInfo);
        }
    }

    private CommandInfo createCommandInfo(CommandParamManager paramManager) {
        final List<ParamAndValue> paramAndValues = createParamAndValues(paramManager);
        final int currentParamIndex = findCurrentParamIndex(paramManager);
        return new CommandInfo(getName(), paramAndValues, currentParamIndex);
    }

    private List<ParamAndValue> createParamAndValues(CommandParamManager paramManager) {
        final List<CommandParam> params = command.getParams();
        final List<ParamAndValue> paramAndValues = new ArrayList<>(params.size());
        for (CommandParam param : params) {
            final Optional<String> value = paramManager.getParamRawValue(param.getName());
            paramAndValues.add(new ParamAndValue(param, value));
        }
        return paramAndValues;
    }

    private int findCurrentParamIndex(CommandParamManager paramManager) {
        final Optional<CommandParam> currentParamOptional = paramManager.getCurrentParam();
        if (!currentParamOptional.isPresent() || !paramManager.hasUnboundParams()) {
            return -1;
        }

        final CommandParam currentParam = currentParamOptional.get();
        final List<CommandParam> params = command.getParams();
        for (int i = 0; i < params.size(); i++) {
            if (currentParam == params.get(i)) {
                return i;
            }
        }
        throw new ShellException(
            "Internal error: The next unbound parameter does not belong to command!? command=%s, param=%s",
            command, currentParam
        );
    }
}
