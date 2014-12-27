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

package com.github.ykrasik.jerminal.internal.annotation;

import com.github.ykrasik.jerminal.api.annotation.ToggleCommand;
import com.github.ykrasik.jerminal.api.command.CommandBuilder;
import com.github.ykrasik.jerminal.api.command.OutputPrinter;
import com.github.ykrasik.jerminal.api.command.parameter.CommandParam;
import com.github.ykrasik.jerminal.api.command.toggle.StateAccessor;
import com.github.ykrasik.jerminal.api.command.toggle.ToggleCommandBuilder;
import com.github.ykrasik.jerminal.api.filesystem.command.Command;
import com.github.ykrasik.jerminal.internal.util.ReflectionUtils;
import com.google.common.base.Optional;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * Creates {@link Command}s out of {@link Method}s if they are annotated.<br>
 * Empty names or descriptions aren't allowed.
 * @see com.github.ykrasik.jerminal.api.annotation.Command Command
 * @see com.github.ykrasik.jerminal.api.annotation.ToggleCommand
 *
 * @author Yevgeny Krasik
 */
public class AnnotationCommandFactory {
    public static final String DEFAULT_DESCRIPTION = "command";
    public static final String DEFAULT_TOGGLE_DESCRIPTION = "toggle";

    private final AnnotationCommandParamFactory paramFactory;

    public AnnotationCommandFactory() {
        this(new AnnotationCommandParamFactory());
    }

    /**
     * For testing.
     */
    AnnotationCommandFactory(AnnotationCommandParamFactory paramFactory) {
        this.paramFactory = Objects.requireNonNull(paramFactory);
    }

    /**
     * @param instance Instance that will act as 'this' when invoking the method through reflection.
     * @param method Method to be processed.
     * @return A {@link Command} if the method is annotated with any supported annotation.
     */
    public Optional<Command> createCommand(Object instance, Method method) {
        // Check if method has the @Command annotation.
        final com.github.ykrasik.jerminal.api.annotation.Command commandAnnotation = method.getAnnotation(com.github.ykrasik.jerminal.api.annotation.Command.class);
        if (commandAnnotation != null) {
            final String name = getOrGenerateCommandName(commandAnnotation.value(), method);
            final String description = getOrGenerateCommandDescription(commandAnnotation.description(), DEFAULT_DESCRIPTION);
            try {
                return Optional.of(createCommand(instance, method, name, description));
            } catch (Exception e) {
                final String message = String.format("Error creating command: class=%s, method=%s", method.getDeclaringClass(), method.getName());
                throw new IllegalArgumentException(message, e);
            }
        }

        // Check if method has the @ToggleCommand annotation.
        final ToggleCommand toggleCommandAnnotation = method.getAnnotation(ToggleCommand.class);
        if (toggleCommandAnnotation != null) {
            final String name = getOrGenerateCommandName(toggleCommandAnnotation.value(), method);
            final String description = getOrGenerateCommandDescription(toggleCommandAnnotation.description(), DEFAULT_TOGGLE_DESCRIPTION);
            try {
                return Optional.of(createToggleCommand(instance, method, name, description));
            } catch (Exception e){
                final String message = String.format("Error creating toggle command: class=%s, method=%s", method.getDeclaringClass(), method.getName());
                throw new IllegalArgumentException(message, e);
            }
        }

        // This method is not annotated with any command annotation.
        return Optional.absent();
    }

    private String getOrGenerateCommandName(String name, Method method) {
        final String trimmedName = name.trim();
        if (!trimmedName.isEmpty()) {
            return trimmedName;
        } else {
            return method.getName();
        }
    }

    private String getOrGenerateCommandDescription(String description, String defaultDescription) {
        final String trimmedDescription = description.trim();
        if (!trimmedDescription.isEmpty()) {
            return trimmedDescription;
        } else {
            return defaultDescription;
        }
    }

    private Command createCommand(Object instance, Method method, String name, String description) {
        final Class<?>[] parameterTypes = method.getParameterTypes();
        final Annotation[][] parameterAnnotations = method.getParameterAnnotations();

        // If present, the outputPrinter must be the first arg.
        final boolean hasOutputPrinter = parameterTypes.length > 0 && isOutputPrinter(parameterTypes[0]);

        final CommandBuilder builder = new CommandBuilder(name);
        builder.setDescription(description);
        builder.setExecutor(new ReflectionCommandExecutor(instance, method, hasOutputPrinter));

        // Create command parameters from method parameters.
        for (int i = hasOutputPrinter ? 1 : 0; i < parameterTypes.length; i++) {
            final Class<?> parameterType = parameterTypes[i];
            if (isOutputPrinter(parameterType)) {
                final String message = String.format(
                    "OutputPrinters must either be the first argument of a method or not be present at all: class=%s, method=%s",
                    method.getDeclaringClass(), method.getName()
                );
                throw new IllegalArgumentException(message);
            }
            final Annotation[] annotations = parameterAnnotations[i];
            final CommandParam param = paramFactory.createCommandParam(instance, parameterType, annotations, i);
            builder.addParam(param);
        }

        return builder.build();
    }

    private boolean isOutputPrinter(Class<?> clazz) {
        return OutputPrinter.class.isAssignableFrom(clazz);
    }

    private Command createToggleCommand(Object instance, Method method, String name, String description) {
        assertReturnValue(method, StateAccessor.class, ToggleCommand.class);
        assertNoParameters(method, ToggleCommand.class);

        final StateAccessor accessor = ReflectionUtils.invokeNoArgs(instance, method, StateAccessor.class);
        final ToggleCommandBuilder builder = new ToggleCommandBuilder(name, accessor);
        builder.setCommandDescription(description);
        return builder.build();
    }

    private void assertReturnValue(Method method, Class<?> expectedReturnValue, Class<?> annotation) {
        final Class<?> returnType = method.getReturnType();
        if (returnType != expectedReturnValue) {
            final String message = String.format(
                "Methods annotated with %s must return a %s: class=%s, method=%s",
                annotation, expectedReturnValue, method.getDeclaringClass(), method.getName()
            );
            throw new IllegalArgumentException(message);
        }
    }

    private void assertNoParameters(Method method, Class<?> annotation) {
        final Class<?>[] parameterTypes = method.getParameterTypes();
        if (parameterTypes.length > 0) {
            final String message = String.format(
                "Methods annotated with %s must not take any parameters: class=%s, method=%s",
                annotation, method.getDeclaringClass(), method.getName()
            );
            throw new IllegalArgumentException(message);
        }
    }
}
