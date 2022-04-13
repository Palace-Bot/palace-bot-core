package org.github.palace.bot.core.collection;

import lombok.val;
import org.github.palace.bot.core.cli.CommandSession;

import java.util.HashMap;
import java.util.Map;

/**
 * @author JHY
 * @date 2022/3/25 8:24
 */
public class GroupContextMap extends HashMap<Long, Map<Long, MemberContextStack<CommandSession>>> {

    public CommandSession put(Long group, Long qq, CommandSession commandSession) {
        val memberMap = this.getOrDefault(group, new HashMap<>(16));
        val memberCommandStack = memberMap.getOrDefault(qq, new MemberContextStack<>(100));
        memberCommandStack.add(commandSession);

        super.put(group, memberMap);
        memberMap.put(qq, memberCommandStack);

        return commandSession;
    }

    public CommandSession get(Long group, Long qq, CommandSession.State state) {
        val commandSessions = get(group, qq);
        if (commandSessions != null) {
            return commandSessions.get(state);
        }
        return null;
    }

    public CommandSession getLast(Long group, Long qq, CommandSession.State state) {
        val commandSessions = get(group, qq);
        if (commandSessions != null) {
            return commandSessions.getLast(state);
        }
        return null;
    }

    public MemberContextStack<CommandSession> get(Long group, Long qq) {
        val memberMap = this.get(group);
        if (memberMap != null) {
            return memberMap.get(qq);
        }
        return null;
    }
}
