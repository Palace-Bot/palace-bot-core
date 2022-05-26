package org.github.palace.bot.core.cli;

import lombok.Getter;
import lombok.Setter;
import org.github.palace.bot.core.plugin.AbstractCommand;

import java.util.concurrent.TimeUnit;

/**
 * 命令执行状态
 *
 * @author JHY
 * @date 2022/3/25 7:31
 */
@Getter
public class CommandSession {

    @Setter
    private AbstractCommand command;

    private State state;

    private final long timeout;

    private long lastTimestamp;

    private final TimeUnit timeUnit;

    public enum State {
        RUNNABLE,
        PREPARE,
        FINISH,
        CRASH
    }

    public CommandSession(AbstractCommand command) {
        this(command, null);
    }

    public CommandSession(AbstractCommand command, State state) {
        this.command = command;
        this.state = state;

        // TODO 读取配置
        visit();
        this.timeUnit = TimeUnit.MINUTES;
        this.timeout = 5;
    }

    public CommandSession runnable() {
        visit();
        this.setState(CommandSession.State.RUNNABLE);
        return this;
    }

    public CommandSession prepare() {
        visit();
        this.setState(CommandSession.State.PREPARE);
        return this;
    }

    public CommandSession crash() {
        visit();
        this.setState(CommandSession.State.CRASH);
        return this;
    }

    public CommandSession finish() {
        visit();
        this.setState(CommandSession.State.FINISH);
        return this;
    }

    public boolean isTimeout(){
        return System.currentTimeMillis() - lastTimestamp > timeUnit.toSeconds(timeout);
    }

    private void visit() {
        this.lastTimestamp = System.currentTimeMillis();
    }

    private void setState(State state) {
        visit();
        this.state = state;
    }

}
