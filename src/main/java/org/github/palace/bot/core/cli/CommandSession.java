package org.github.palace.bot.core.cli;

import lombok.Getter;
import lombok.Setter;
import net.mamoe.mirai.message.data.MessageChain;

/**
 * 命令执行状态
 *
 * @author JHY
 * @date 2022/3/25 7:31
 */

public class CommandSession {

    @Getter
    @Setter
    private AbstractCommand command;

    @Getter
    private State state;

    @Getter
    private final MessageChain chain;

    public enum State {
        RUNNABLE,
        PREPARE,
        FINISH,
        CRASH
    }

    public CommandSession(AbstractCommand command, MessageChain chain) {
        this.command = command;
        this.chain = chain;
    }

    public CommandSession(AbstractCommand command, MessageChain chain, State state) {
        this.command = command;
        this.chain = chain;
        this.state = state;
    }

    public CommandSession runnable() {
        this.setState(CommandSession.State.RUNNABLE);
        return this;
    }

    public CommandSession prepare() {
        this.setState(CommandSession.State.PREPARE);
        return this;
    }

    public CommandSession crash() {
        this.setState(CommandSession.State.CRASH);
        return this;
    }

    public CommandSession finish() {
        this.setState(CommandSession.State.FINISH);
        return this;
    }

    private void setState(State state) {
        this.state = state;
    }

}
