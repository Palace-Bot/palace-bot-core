package org.github.palace.bot.core.collection;

import lombok.val;
import org.github.palace.bot.core.cli.CommandSession;

import java.util.HashMap;
import java.util.Map;

/**
 * @author JHY
 * @date 2022/3/25 8:24
 */
public class GroupContextMap extends HashMap<Long, Map<Long, MemberContextQueue<CommandSession>>> {

    public CommandSession put(Long group, Long qq, CommandSession commandSession) {
        val memberMap = this.getOrDefault(group, new HashMap<>(16));
        val memberCommandQueue = memberMap.getOrDefault(qq, new MemberContextQueue<>(100));
        memberCommandQueue.add(commandSession);

        super.put(group, memberMap);
        memberMap.put(qq, memberCommandQueue);

        return commandSession;
    }

    public CommandSession get(Long group, Long qq, CommandSession.State state) {
        val memberMap = this.get(group);

        if (memberMap == null) return null;
        MemberContextQueue<CommandSession> commandSessions = memberMap.get(qq);

        if (commandSessions == null) return null;
        return commandSessions.get(state);
    }

}
