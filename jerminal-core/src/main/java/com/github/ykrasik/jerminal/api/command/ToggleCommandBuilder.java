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

package com.github.ykrasik.jerminal.api.command;

import com.google.common.base.Supplier;
import com.github.ykrasik.jerminal.api.command.parameter.bool.BooleanParamBuilder;
import com.github.ykrasik.jerminal.api.exception.ExecuteException;

/**
 * Creates toggle {@link Command}s.<br>
 * A toggle command is a commmand that takes a single optional boolean parameter and toggles
 * the boolean state of some component on or off. The state of the component is accessed via a
 * {@link StateAccessor}.<br>
 * If the optional boolean parameter is passed, the toggle command will set the {@link StateAccessor}'s
 * state to whatever value the parameter had. If boolean parameter is not passed, the toggle command
 * will toggle the state of the {@link StateAccessor} - If it was previously 'false', it will now be 'true'
 * and vice versa.
 *
 * @author Yevgeny Krasik
 */
public class ToggleCommandBuilder {
    private static final String PARAM_NAME = "state";

    private final String name;
    private final StateAccessor accessor;
    private final ShellCommandBuilder builder;

    private String paramDescription = "toggle";

    public ToggleCommandBuilder(String name, StateAccessor accessor) {
        this.name = name;
        this.accessor = accessor;
        this.builder = new ShellCommandBuilder(name);
        this.builder.setDescription("toggle");
    }

    public Command build() {
        return builder
            .addParam(new BooleanParamBuilder(PARAM_NAME)
                    .setDescription(paramDescription)
                    .setOptional(new AccessorDefaultValueProvider(accessor))
                    .build()
            )
            .setExecutor(new CommandExecutor() {
                @Override
                public void execute(CommandArgs args, OutputPrinter outputPrinter) throws ExecuteException {
                    final boolean toggle = args.getBool(PARAM_NAME);
                    accessor.set(toggle);
                    outputPrinter.println("%s: %s", name, toggle);
                }
            })
            .build();
    }

    public ToggleCommandBuilder setCommandDescription(String description) {
        builder.setDescription(description);
        return this;
    }

    public ToggleCommandBuilder setParameterDescription(String description) {
        this.paramDescription = description;
        return this;
    }

    /**
     * Accesses the boolean state of a component.
     *
     * @author Yevgeny Krasik
     */
    public interface StateAccessor {
        /**
         * Sets the state of the component.
         */
        void set(boolean value);

        /**
         * Returns the current state of the component.
         */
        boolean get();
    }

    private static class AccessorDefaultValueProvider implements Supplier<Boolean> {
        private final StateAccessor accessor;

        private AccessorDefaultValueProvider(StateAccessor accessor) {
            this.accessor = accessor;
        }

        @Override
        public Boolean get() {
            return accessor.get();
        }
    }
}
