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

package com.github.ykrasik.jaci.reflection.method;

import com.badlogic.gdx.utils.reflect.Method;
import com.github.ykrasik.jaci.command.CommandDef;
import com.github.ykrasik.jaci.command.CommandOutputPromise;
import com.github.ykrasik.jaci.reflection.method.factory.DefaultAnnotationMethodCommandFactory;
import com.github.ykrasik.jaci.reflection.method.factory.MethodCommandFactory;
import com.github.ykrasik.jaci.reflection.method.factory.ToggleAnnotationMethodCommandFactory;
import com.github.ykrasik.jaci.util.opt.Opt;
import com.badlogic.gdx.Gdx;


import java.util.Arrays;
import java.util.List;

/**
 * Creates {@link CommandDef}s out of {@link Method}s if they are accepted by one of the {@link MethodCommandFactory}s.
 *
 * @author Yevgeny Krasik
 */
public class ReflectionMethodProcessor {
    private final List<MethodCommandFactory> factories;

    /**
     * @param outputPromise An {@link CommandOutputPromise} that will be injected into the instance containing the methods.
     */
    public ReflectionMethodProcessor( CommandOutputPromise outputPromise) {
        this(new DefaultAnnotationMethodCommandFactory(outputPromise), new ToggleAnnotationMethodCommandFactory());
    }

    /**
     * Package-protected for testing.
     */
    ReflectionMethodProcessor( MethodCommandFactory... factories) {
        this.factories = Arrays.asList(factories);
    }

    /**
     * Process the method and create a {@link CommandDef} out of it, if it is accepted by one of the {@link MethodCommandFactory}s.
     *
     * @param instance Instance of a class to which this method belongs.
     * @param method Method to be processed.
     * @return A {@code present} {@link CommandDef} if any of the factories managed to process the method.
     */
    public Opt<CommandDef> process( Object instance,  Method method) {
        try {
            return doCreateCommand(instance, method);
        } catch (Exception e) {
            final String message = ("Error creating command: class="+method.getDeclaringClass()+", method="+ method.getName());
            throw new IllegalArgumentException(message, e);
        }
    }

    private Opt<CommandDef> doCreateCommand(Object instance, Method method) throws Exception {
        for (MethodCommandFactory factory : factories) {
            final Opt<CommandDef> commandDef = factory.create(instance, method);
            if (commandDef.isPresent()) {
                return commandDef;
            }
        }

        // None of the registered factories managed to create a commandDef out of the method.
        return Opt.absent();
    }
}
