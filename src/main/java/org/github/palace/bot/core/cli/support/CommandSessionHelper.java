package org.github.palace.bot.core.cli.support;

import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.MessageSource;
import net.mamoe.mirai.message.data.SingleMessage;
import org.github.palace.bot.core.CommandScope;
import org.github.palace.bot.core.collection.IdMap;
import org.github.palace.bot.core.plugin.AbstractCommand;
import org.github.palace.bot.core.cli.CommandSession;
import org.github.palace.bot.core.collection.GroupMemberMap;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

import static org.github.palace.bot.core.cli.CommandSession.State;

/**
 * 管理命令执行状态
 *
 * @author JHY
 * @date 2022/3/28 9:02
 */
public class CommandSessionHelper {

    /**
     * Save member command session
     */
    private final IdMap<? super CommandSession> memberMap = new IdMap<>();

    /**
     * Save group command session
     */
    private final IdMap<? super CommandSession> groupMap = new IdMap<>();

    /**
     * Save {@code group -> member} command session
     */
    private final GroupMemberMap<? super CommandSession> groupMemberMap = new GroupMemberMap<>();


    /**
     * Put session
     *
     * @param messageSource 消息源
     * @param command       command
     * @return commandSession
     */
    public CommandSession put(MessageSource messageSource, AbstractCommand command) {
        CommandSession commandSession = new CommandSession(command).runnable();
        CommandScope scope = command.getScope();
        switch (scope) {
            case MEMBER:
                memberMap.put(messageSource.getFromId(), commandSession);
                break;
            case GROUP:
                groupMap.put(messageSource.getFromId(), commandSession);
                break;
            case GROUP_MEMBER:
                groupMemberMap.put(messageSource.getFromId(), messageSource.getFromId(), commandSession);
                break;
        }
        return commandSession;
    }

    /**
     * Get command session by state
     *
     * @param messageSource 消息源
     * @param state         session state
     * @return sort -> group member -> group -> member
     */
    @Nonnull
    public List<CommandSession> get(MessageSource messageSource, State state) {
        List<CommandSession> sessions = new ArrayList<>();

        CommandSession session = groupMemberMap.get(messageSource.getFromId(), messageSource.getFromId(), state);
        if (session != null) {
            sessions.add(session);
        }

        session = groupMap.get(messageSource.getFromId(), state);
        if (session != null) {
            sessions.add(session);
        }

        session = memberMap.get(messageSource.getFromId(), state);
        if (session != null) {
            sessions.add(session);
        }
        return sessions;
    }

    public CommandSession prepare(CommandSession commandSession) {
        return commandSession.prepare();
    }

    public CommandSession crash(CommandSession commandSession) {
        return commandSession.crash();
    }

    public CommandSession finish(CommandSession commandSession) {
        return commandSession.finish();
    }

    public CommandSession trySendDetermine(Group subject, MessageSource messageSource, SingleMessage singleMessage) {
        String determine = singleMessage.contentToString().trim().toLowerCase();

        List<CommandSession> sessions = this.get(messageSource, State.PREPARE);

        At at = new At(messageSource.getFromId());
        for (CommandSession session : sessions) {
            if ("y".equals(determine) || "yes".equals(determine)) {
                subject.sendMessage(at.plus(" ").plus(session.getCommand().getPrimaryName()).plus(" 命令正在处理中..."));
                return session;
            } else if ("n".equals(determine) || "no".equals(determine)) {
                subject.sendMessage(at.plus(" ").plus(session.getCommand().getPrimaryName()).plus(" 命令已拒绝"));
            }
        }
        return null;
    }
}
