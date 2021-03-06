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
package com.github.ykrasik.jaci.reflection;

import com.badlogic.gdx.utils.reflect.*;
import com.github.ykrasik.jaci.api.CommandOutput;
import com.github.ykrasik.jaci.api.CommandPath;
import com.github.ykrasik.jaci.command.CommandDef;
import com.github.ykrasik.jaci.command.CommandOutputPromise;
import com.github.ykrasik.jaci.path.ParsedPath;
import com.github.ykrasik.jaci.reflection.method.ReflectionMethodProcessor;
import com.github.ykrasik.jaci.util.opt.Opt;

import java.util.*;

/**
 * Processes a class and creates {@link CommandDef}s from qualifying methods.
 *
 * @author Yevgeny Krasik
 */
public class ReflectionClassProcessor {
    
    /**
     * Will be injected into all processed instances.
     */
    private final CommandOutputPromise outputPromise;
    private final ReflectionMethodProcessor methodProcessor;

    public ReflectionClassProcessor() {
        this(new CommandOutputPromise());
    }

    /**
     * Package-protected for testing.
     */
    ReflectionClassProcessor(CommandOutputPromise outputPromise) {
        this(outputPromise, new ReflectionMethodProcessor(outputPromise));
    }

    /**
     * Package-protected for testing.
     */
    ReflectionClassProcessor( CommandOutputPromise outputPromise,  ReflectionMethodProcessor methodProcessor) {
        if (outputPromise == null) {
            throw new java.lang.NullPointerException("outputPromise");
        }
        if (methodProcessor == null) {
            throw new java.lang.NullPointerException("methodProcessor");
        }
        this.outputPromise = outputPromise;
        this.methodProcessor = methodProcessor;
    }

    /**
     * Process the object and return a {@link Map} from a {@link ParsedPath} to a {@link List} of {@link CommandDef}s
     * that were defined for that path.
     *
     * @param instance Object to process.
     * @return The {@link CommandDef}s that were extracted out of the object.
     * @throws RuntimeException If any error occurs.
     */
    public Map<ParsedPath, List<CommandDef>> processObject( Object instance) {
        try {
            if (instance == null) {
                throw new java.lang.NullPointerException("instance");
            }
            final Class<?> clazz = instance.getClass();
            // Inject our outputPromise into the processed instance.
            // Any commands declared in the instance will reference this outputPromise, which will eventually
            // contain a concrete implementation of a CommandOutput.
            injectOutputPromise(instance, clazz);
            // All method paths will be appended to the class's top level path.
            final ParsedPath topLevelPath = getTopLevelPath(clazz);
            final ClassContext context = new ClassContext(topLevelPath);
            // Create commands from all qualifying methods..
            final Method[] methods = ClassReflection.getMethods(clazz);
            for (Method method : methods) {
                processMethod(context, instance, method);
            }
            return context.commandPaths;
        } catch (final java.lang.Throwable $ex) {
            throw new RuntimeException($ex);
        }
    }

    private void injectOutputPromise(Object instance, Class<?> clazz) throws IllegalAccessException, ReflectionException {
        for (Field field : ClassReflection.getDeclaredFields(clazz)) {
            if (field.getType() == CommandOutput.class) {
                if (!field.isAccessible()) {
                    field.setAccessible(true);
                }
                field.set(instance, outputPromise);
            }
        }
    }

    private void processMethod(ClassContext context, Object instance, Method method) {
        // Commands can only be created from qualifying methods.
        final Opt<CommandDef> commandDef = methodProcessor.process(instance, method);
        if (!commandDef.isPresent()) {
            return;
        }
        final ParsedPath commandPath = getCommandPath(method);
        context.addCommandDef(commandPath, commandDef.get());
    }

    private ParsedPath getTopLevelPath(Class<?> clazz) {
        Annotation annotation = ClassReflection.getAnnotation(clazz, (CommandPath.class));
        return getPathFromAnnotation(annotation==null?null:annotation.getAnnotation(CommandPath.class));
    }

    private ParsedPath getCommandPath(Method method) {
        Annotation declaredAnnotation = method.getDeclaredAnnotation(CommandPath.class);
        return getPathFromAnnotation(declaredAnnotation==null?null:declaredAnnotation.getAnnotation(CommandPath.class));
    }

    private ParsedPath getPathFromAnnotation(CommandPath annotation) {
        if (annotation != null) {
            return ParsedPath.toDirectory(annotation.value());
        } else {
            // Annotation isn't present, set the default path to 'root'.
            // Composing any path with 'root' has no effect.
            return ParsedPath.root();
        }
    }

    /**
     * Auxiliary class for collecting {@link CommandDef}s.
     */
    private static class ClassContext {
        private final ParsedPath topLevelPath;
        private final Map<ParsedPath, List<CommandDef>> commandPaths = new HashMap<>();
        /**
         * Add a {@link CommandDef} to the given {@link ParsedPath}.
         *
         * @param path Path to add the command to.
         * @param commandDef CommandDef to add.
         */
        public void addCommandDef(ParsedPath path, CommandDef commandDef) {
            // Compose the top level path of the declaring class with the command path.
            final ParsedPath composedPath = topLevelPath.append(path);
            List<CommandDef> commands = commandPaths.get(composedPath);
            if (commands == null) {
                commands = new ArrayList<>();
                commandPaths.put(composedPath, commands);
            }
            commands.add(commandDef);
        }

        //@java.beans.ConstructorProperties({"topLevelPath"})
        @java.lang.SuppressWarnings("all")
        @javax.annotation.Generated("lombok")
        public ClassContext(final ParsedPath topLevelPath) {
            this.topLevelPath = topLevelPath;
        }
    }
}