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
import com.github.ykrasik.jemi.cli.output.CliPrinter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * @author Yevgeny Krasik
 */
// TODO: JavaDoc
@RequiredArgsConstructor
public class CliCommandOutputImpl implements CliCommandOutput {
    @NonNull private final CliPrinter printer;

    private boolean printDefaultExecutionMessage = true;

    @Override
    public boolean isPrintDefaultExecutionMessage() {
        return printDefaultExecutionMessage;
    }

    @Override
    public void setWorkingDirectory(CliDirectory directory) {
        printer.setWorkingDirectory(directory);
        suppressDefaultExecutionMessage();
    }

    @Override
    public void printDirectory(CliDirectory directory, boolean recursive) {
        printer.printDirectory(directory, recursive);
        suppressDefaultExecutionMessage();
    }

    @Override
    public void printCommand(CliCommand command) {
        printer.printCommand(command);
        suppressDefaultExecutionMessage();
    }

    @Override
    public void message(String text) {
        printer.println(text);
        suppressDefaultExecutionMessage();
    }

    @Override
    public void message(String format, Object... args) {
        message(String.format(format, args));
    }

    @Override
    public void error(String text) {
        printer.errorPrintln(text);
        suppressDefaultExecutionMessage();
    }

    @Override
    public void error(String format, Object... args) {
        error(String.format(format, args));
    }

    private void suppressDefaultExecutionMessage() {
        // Called any time there is any interaction with this output.
        // The default message should only be printed if the command didn't print anything by itself.
        printDefaultExecutionMessage = false;
    }
}