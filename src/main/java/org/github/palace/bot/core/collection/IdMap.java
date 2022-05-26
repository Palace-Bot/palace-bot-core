package org.github.palace.bot.core.collection;

import lombok.val;
import org.github.palace.bot.core.cli.CommandSession;

import javax.annotation.Nullable;
import java.util.HashMap;

import static org.github.palace.bot.core.cli.CommandSession.State;

/**
 * @author jihongyuan
 * @date 2022/5/24 9:05
 */
public class IdMap<V extends CommandSession> extends HashMap<Long, CommonSessionContext<V>> {

    @Nullable
    public V get(Long id, State state) {
        val stack = this.get(id);
        if (stack != null) {
            return stack.get(state);
        }
        return null;
    }

    public V getLast(Long id, State state) {
        val stack = this.get(id);
        if (stack != null) {
            return stack.getLast(state);
        }
        return null;
    }

    public V put(Long id, V commandSession) {
        val stack = super.computeIfAbsent(id, k -> new CommonSessionContext<>(100));
        stack.add(commandSession);
        return commandSession;
    }

    @Nullable
    private CommonSessionContext<V> get(Long id) {
        return super.get(id);
    }

}
