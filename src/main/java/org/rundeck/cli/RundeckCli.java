/*
 * Copyright 2011 Vincent Behar
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.rundeck.cli;

import org.apache.commons.lang.StringUtils;
import org.rundeck.api.RundeckClient;
import org.rundeck.cli.command.Command;
import org.rundeck.cli.command.ListCommand;
import org.rundeck.cli.command.StatusCommand;
import com.beust.jcommander.JCommander;

/**
 * Main entry point for the RunDeck CLI program
 * 
 * @author Vincent Behar
 */
public class RundeckCli {

    /** CLI arguments */
    private final String[] args;

    /** All available commands */
    private final Command[] commands;

    /**
     * Build a new CLI instance for the given commands and arguments
     * 
     * @param args CLI arguments
     * @param commands all available {@link Command}s
     */
    public RundeckCli(String[] args, Command... commands) {
        this.args = args;
        this.commands = commands;
    }

    /**
     * Run the program : parse the arguments and execute the selected command
     */
    public void run() {
        RundeckClientFactory rundeckFactory = new RundeckClientFactory();

        // load commands
        JCommander jCommander = new JCommander(rundeckFactory);
        for (Command command : commands) {
            jCommander.addCommand(command.getName(), command, command.getAliases());
        }

        // parse arguments
        jCommander.parse(args);

        // and execute the selected command
        for (Command command : commands) {
            if (StringUtils.equals(jCommander.getParsedCommand(), command.getName())) {
                execute(command, rundeckFactory);
                return;
            }
        }

        // no command selected : display the usage message
        jCommander.usage();
    }

    /**
     * Execute the given {@link Command}
     * 
     * @param command to execute
     * @param rundeckFactory for getting a {@link RundeckClient} instance
     */
    private void execute(Command command, RundeckClientFactory rundeckFactory) {
        RundeckClient rundeckClient = rundeckFactory.instantiateRundeckClient();
        command.execute(rundeckClient);
    }

    /**
     * Main entry point !
     * 
     * @param args CLI arguments
     */
    public static void main(String[] args) {
        new RundeckCli(args, new StatusCommand(), new ListCommand()).run();
    }
}
