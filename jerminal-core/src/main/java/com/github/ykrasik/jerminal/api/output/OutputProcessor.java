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

package com.github.ykrasik.jerminal.api.output;

import com.rawcod.jerminal.command.view.ShellCommandView;
import com.github.ykrasik.jerminal.internal.filesystem.view.ShellEntryView;
import com.rawcod.jerminal.returnvalue.parse.ParseError;

import java.util.List;

/**
 * In charge of displaying information to the user.<br>
 * Receives it's commands from the {@link com.github.ykrasik.jerminal.api.Shell Shell} to which it is attached.<br>
 * In an MVC architecture, this this the view.
 *
 * @author Yevgeny Krasik
 */
public interface OutputProcessor {
    /**
     * Display the welcome message.
     */
    void displayWelcomeMessage(String welcomeMessage);

    /**
     * Clear the command line.
     */
    void clearCommandLine();

    /**
     * Set the command line to the given command line.
     */
    void setCommandLine(String commandLine);

    /**
     * The user typed in a blank command line. The user probably just wants a new blank line.
     */
    void handleBlankCommandLine();

    /**
     * A parse error occurred while parsing the command line.
     */
    void parseError(ParseError error, String errorMessage);

    /**
     * The user requested assistance with the command line, but no assistance can be given.
     */
    void autoCompleteNotPossible(String errorMessage);

    /**
     * An execution error occurred while executing the command line.<br>
     * This can only happen when the user explicitly throws an {@link com.rawcod.jerminal.exception.ExecuteException ExecuteException}.
     */
    void executeError(String errorMessage);

    /**
     * An unhandled exception was thrown while executing the command line.<br>
     * This is not an internal error -
     * this exception was thrown from within the code associated with the command being run.
     */
    void executeUnhandledException(Exception e);

    /**
     * Either the user requested assistance, or an error occurred. Display possible suggestions.
     */
    void displaySuggestions(List<String> directorySuggestions,
                            List<String> commandSuggestions,
                            List<String> paramNameSuggestions,
                            List<String> paramValueSuggestions);

    /**
     * Display the output generated by executing the command line.
     */
    void displayCommandOutput(List<String> output);

    /**
     * The user requested to display the directory structure of a directory.
     */
    void displayShellEntryView(ShellEntryView shellEntryView);

    /**
     * The user requested to display information about a command.
     */
    void displayShellCommandView(ShellCommandView shellCommandView);

    // TODO: Add a 'setPath' call, for 'cd'.
}
