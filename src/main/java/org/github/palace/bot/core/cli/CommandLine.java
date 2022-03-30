package org.github.palace.bot.core.cli;

import lombok.Getter;
import lombok.Setter;

/**
 * @author JHY
 * @date 2022/3/25 7:31
 */
public class CommandLine {

    @Getter
    @Setter
    private Command command;

    @Getter
    private State state;

    public enum State {
        RUNNABLE,
        PREPARE,
        FINISH,
        CRASH
    }

    public CommandLine(Command command) {
        this.command = command;
    }

    public CommandLine(Command command, State state) {
        this.command = command;
        this.state = state;
    }

    public CommandLine runnable() {
        this.setState(CommandLine.State.RUNNABLE);
        return this;
    }

    public CommandLine prepare() {
        this.setState(CommandLine.State.PREPARE);
        return this;
    }

    public CommandLine crash() {
        this.setState(CommandLine.State.CRASH);
        return this;
    }

    public CommandLine finish() {
        this.setState(CommandLine.State.FINISH);
        return this;
    }


    private void setState(State state) {
        this.state = state;
    }
}
