package org.github.palace.bot.core.collection;

import lombok.val;
import org.github.palace.bot.core.cli.CommandSession;

import javax.annotation.Nullable;
import java.util.HashMap;

import static org.github.palace.bot.core.cli.CommandSession.State;

/**
 * @author JHY
 * @date 2022/3/25 8:24
 */
public class GroupMemberMap<V extends CommandSession> extends HashMap<Long, IdMap<V>> {


    public V get(Long group, Long qq, State state) {
        val commandSessions = this.get(group, qq);
        if (commandSessions != null) {
            return commandSessions.get(state);
        }
        return null;
    }

    public V getLast(Long group, Long qq, State state) {
        val commandSessions = this.get(group, qq);
        if (commandSessions != null) {
            return commandSessions.getLast(state);
        }
        return null;
    }

    public V put(Long group, Long qq, V commandSession) {
        val groupMap = super.computeIfAbsent(group, k -> new IdMap<>());
        groupMap.put(qq, commandSession);
        return commandSession;
    }

    @Nullable
    private CommonSessionContext<V> get(Long group, Long qq) {
        val memberMap = super.get(group);
        if (memberMap != null) {
            return memberMap.get(qq);
        }
        return null;
    }

}
