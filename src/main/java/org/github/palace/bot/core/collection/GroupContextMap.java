package org.github.palace.bot.core.collection;

import lombok.val;
import org.github.palace.bot.core.cli.CommandLine;

import java.util.HashMap;
import java.util.Map;

/**
 * @author JHY
 * @date 2022/3/25 8:24
 */
public class GroupContextMap extends HashMap<Long, Map<Long, MemberContextQueue<CommandLine>>> {

    public CommandLine put(Long group, Long qq, CommandLine commandLine) {
        val memberMap = this.getOrDefault(group, new HashMap<>(16));
        val memberCommandQueue = memberMap.getOrDefault(qq, new MemberContextQueue<>(100));
        memberCommandQueue.add(commandLine);

        super.put(group, memberMap);
        memberMap.put(qq, memberCommandQueue);

        return commandLine;
    }

    public CommandLine get(Long group, Long qq, CommandLine.State state) {
        val memberMap = this.get(group);

        if (memberMap == null) return null;
        MemberContextQueue<CommandLine> commandLines = memberMap.get(qq);

        if (commandLines == null) return null;
        return commandLines.get(state);
    }

}
