// Generated by delombok at Fri Jan 01 22:07:36 EST 2016
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
package com.github.ykrasik.jaci.cli.param;

import com.github.ykrasik.jaci.Identifier;
import com.github.ykrasik.jaci.cli.assist.AutoComplete;
import com.github.ykrasik.jaci.cli.directory.CliDirectory;
import com.github.ykrasik.jaci.cli.exception.ParseException;
import com.github.ykrasik.jaci.cli.hierarchy.CliCommandHierarchy;
import com.github.ykrasik.jaci.util.function.Spplr;
import com.github.ykrasik.jaci.util.function.MoreSuppliers;
import com.github.ykrasik.jaci.util.opt.Opt;


/**
 * A {@link CliParam} that parses {@link CliDirectory} values.
 * Not a part of the official API - this is a CLI-only param which doesn't have a ParamDef,
 * which is why it can only be constructed through the {@link Builder}.
 *
 * @author Yevgeny Krasik
 */
public class DirectoryCliParam extends AbstractCliParam<CliDirectory> {
    private final CliCommandHierarchy hierarchy;

    private DirectoryCliParam(Identifier identifier, Opt<Spplr<CliDirectory>> defaultValueSupplier,  CliCommandHierarchy hierarchy) {
        super(identifier, defaultValueSupplier);
        if (hierarchy == null) {
            throw new java.lang.NullPointerException("hierarchy");
        }
        this.hierarchy = hierarchy;
    }

    @Override
    protected String getValueTypeName() {
        return "directory";
    }

    @Override
    public CliDirectory parse( String arg) throws ParseException {
        if (arg == null) {
            throw new java.lang.NullPointerException("arg");
        }
        return hierarchy.parsePathToDirectory(arg);
    }

    @Override
    public AutoComplete autoComplete( String prefix) throws ParseException {
        if (prefix == null) {
            throw new java.lang.NullPointerException("prefix");
        }
        return hierarchy.autoCompletePathToDirectory(prefix);
    }

    /**
     * A builder for a {@link DirectoryCliParam}.
     */
    public static class Builder {
        private final String name;
        private final CliCommandHierarchy hierarchy;
        private String description = "directory";
        private Opt<Spplr<CliDirectory>> defaultValueSupplier = Opt.absent();

        public Builder( String name,  CliCommandHierarchy hierarchy) {
            if (name == null) {
                throw new java.lang.NullPointerException("name");
            }
            if (hierarchy == null) {
                throw new java.lang.NullPointerException("hierarchy");
            }
            this.name = name;
            this.hierarchy = hierarchy;
        }
        /**
         * @param description Parameter description.
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
         * Set this parameter to be optional, and return the given {@link CliDirectory} if it is not passed.
         *
         * @param defaultValue {@link CliDirectory} to return if the parameter isn't passed.
         * @return {@code this}, for chaining.
         */

        public Builder setOptional( CliDirectory defaultValue) {
            if (defaultValue == null) {
                throw new java.lang.NullPointerException("defaultValue");
            }
            return setOptional(MoreSuppliers.of(defaultValue));
        }
        /**
         * Set this parameter to be optional, and invoke the given {@link Spplr} for a default value if it is not passed.
         *
         * @param defaultValueSupplier Supplier to invoke if the parameter isn't passed.
         * @return {@code this}, for chaining.
         */

        public Builder setOptional( Spplr<CliDirectory> defaultValueSupplier) {
            if (defaultValueSupplier == null) {
                throw new java.lang.NullPointerException("defaultValueSupplier");
            }
            this.defaultValueSupplier = Opt.of(defaultValueSupplier);
            return this;
        }
        /**
         * @return A {@link DirectoryCliParam} built out of this builder's parameters.
         */

        public DirectoryCliParam build() {
            final Identifier identifier = new Identifier(name, description);
            return new DirectoryCliParam(identifier, defaultValueSupplier, hierarchy);
        }

        @java.lang.Override
        @java.lang.SuppressWarnings("all")
        @javax.annotation.Generated("lombok")
        public java.lang.String toString() {
            return "DirectoryCliParam.Builder(name=" + this.name + ", hierarchy=" + this.hierarchy + ", description=" + this.description + ", defaultValueSupplier=" + this.defaultValueSupplier + ")";
        }
    }
}