// Generated by delombok at Fri Jan 01 22:07:36 EST 2016
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
package com.github.ykrasik.jaci.directory;

import com.github.ykrasik.jaci.Identifiable;
import com.github.ykrasik.jaci.Identifier;
import com.github.ykrasik.jaci.command.CommandDef;
import com.github.ykrasik.jaci.util.opt.Opt;
import com.github.ykrasik.jaci.util.string.StringUtils;

import java.util.*;

/**
 * A definition for a command directory.
 * Contains the directory's name, description, child {@link CommandDirectoryDef}s and child {@link CommandDef}s.
 * Built through the {@link CommandDirectoryDef.Builder} builder.
 *
 * @author Yevgeny Krasik
 */
public class CommandDirectoryDef implements Identifiable {
    private final Identifier identifier;
    private final List<CommandDirectoryDef> directoryDefs;
    private final List<CommandDef> commandDefs;

    @Override
    public Identifier getIdentifier() {
        return identifier;
    }

    /**
     * @return Child {@link CommandDirectoryDef}s.
     */
    public List<CommandDirectoryDef> getDirectoryDefs() {
        return directoryDefs;
    }

    /**
     * @return Child {@link CommandDef}s.
     */
    public List<CommandDef> getCommandDefs() {
        return commandDefs;
    }

    @Override
    public String toString() {
        return identifier.toString();
    }

    /**
     * A builder for a {@link CommandDirectoryDef}.
     */
    public static class Builder {
        private final Map<String, Builder> childDirectories = new HashMap<>();
        private final Map<String, CommandDef> childCommands = new HashMap<>();
        private final String name;
        private String description = "directory";
        /**
         * @param name directory name.
         */

        public Builder( String name) {
            if (name == null) {
                throw new java.lang.NullPointerException("name");
            }
            this.name = name;
        }
        /**
         * Set the directory's description.
         *
         * @param description Description to set.
         * @return {@code this}, for chaining.
         */

        public Builder setDescription( String description) {
            if (description == null) {
                throw new java.lang.NullPointerException("description");
            }
            this.description = description;
            return this;
        }
        /**
         * If a directory with the given name is already a child directory of this directory, will return
         * the existing child. Otherwise, will create a new child directory with that name and return it.
         *
         * @param name Directory name.
         * @return An existing or newly created child {@link Builder}.
         * @throws IllegalArgumentException If the name is empty or clashes with an existing child commandDef.
         */

        public Builder getOrCreateDirectory( String name) {
            if (name == null) {
                throw new java.lang.NullPointerException("name");
            }
            final Opt<String> nonEmptyName = StringUtils.getNonEmptyString(name);
            if (!nonEmptyName.isPresent()) {
                throw new IllegalArgumentException("Empty or all-whitespace name is invalid!");
            }
            final String dirName = nonEmptyName.get();
            final Builder existingDirectory = childDirectories.get(dirName);
            if (existingDirectory != null) {
                return existingDirectory;
            }
            assertLegalName(dirName);
            final Builder newDirectory = new Builder(dirName);
            childDirectories.put(dirName, newDirectory);
            return newDirectory;
        }
        /**
         * Add child {@link CommandDef}s to this directory.
         *
         * @param commandDefs CommandDefs to add.
         * @return {@code this}, for chaining.
         * @throws IllegalArgumentException If any of the commandDefs' names clash with existing commandDefs under this directory.
         */

        public Builder addCommandDefs(CommandDef... commandDefs) {
            return addCommandDefs(Arrays.asList(commandDefs));
        }
        /**
         * Add child {@link CommandDef}s to this directory.
         *
         * @param commandDefs CommandDefs to add.
         * @return {@code this}, for chaining.
         * @throws IllegalArgumentException If any of the commandDefs' names clash with existing commandDefs under this directory.
         */

        public Builder addCommandDefs(List<CommandDef> commandDefs) {
            for (CommandDef commandDef : commandDefs) {
                final String commandName = commandDef.getIdentifier().getName();
                assertLegalName(commandName);
                childCommands.put(commandName, commandDef);
            }
            return this;
        }

        private void assertLegalName(String name) {
            if (childDirectories.containsKey(name) || childCommands.containsKey(name)) {
                throw new IllegalArgumentException("Directory \'"+this.name+"\' already contains child entry: \'"+name+"\'");
            }
        }
        /**
         * @return A {@link CommandDirectoryDef} built out of this builder's parameters.
         */

        public CommandDirectoryDef build() {
            final Identifier identifier = new Identifier(name, description);
            final List<CommandDirectoryDef> directories = buildDirectories();
            final List<CommandDef> commandDefs = buildCommandDefs();
            return new CommandDirectoryDef(identifier, directories, commandDefs);
        }

        private List<CommandDirectoryDef> buildDirectories() {
            final List<CommandDirectoryDef> directories = new ArrayList<>(childDirectories.size());
            for (Builder builder : childDirectories.values()) {
                directories.add(builder.build());
            }
            return Collections.unmodifiableList(directories);
        }

        private List<CommandDef> buildCommandDefs() {
            return Collections.unmodifiableList(new ArrayList<>(childCommands.values()));
        }

        @java.lang.Override
        @java.lang.SuppressWarnings("all")
        @javax.annotation.Generated("lombok")
        public java.lang.String toString() {
            return "CommandDirectoryDef.Builder(childDirectories=" + this.childDirectories + ", childCommands=" + this.childCommands + ", name=" + this.name + ", description=" + this.description + ")";
        }
    }

    @java.lang.SuppressWarnings("all")
    @javax.annotation.Generated("lombok")
    private CommandDirectoryDef(final Identifier identifier, final List<CommandDirectoryDef> directoryDefs, final List<CommandDef> commandDefs) {
        this.identifier = identifier;
        this.directoryDefs = directoryDefs;
        this.commandDefs = commandDefs;
    }
}