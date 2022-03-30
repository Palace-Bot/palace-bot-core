package org.github.palace.bot.core.cli;

/**
 * @author JHY
 * @date 2022/3/30 16:30
 */
public abstract class SimpleCommand {

    private final Command command;

    protected SimpleCommand(Command command) {
        this.command = command;
    }

    protected SimpleCommand(Command.CommandBuilder builder) {
        this.command = builder.build();
    }

}
