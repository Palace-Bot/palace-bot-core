package org.github.palace.bot.core.collection;


import org.github.palace.bot.core.cli.CommandSession;

import java.util.*;

/**
 * @author JHY
 * @date 2022/3/25 7:27
 */
public final class MemberContextQueue<T extends CommandSession> extends LinkedList<T> {
    private final int initialCapacity;

    public MemberContextQueue(int initialCapacity) {
        this.initialCapacity = initialCapacity;
    }

    @Override
    public boolean add(T t) {
        // 保证最大容量不会超过 initialCapacity
        if (size() == initialCapacity) {
            poll();
        }
        return super.add(t);
    }

    /**
     * 查找元素状态等于{@code status} 的 最新被添加进 队列中的一条记录
     *
     * @param state 状态
     * @return element
     */
    public T get(CommandSession.State state) {
        for (int i = super.size() - 1; i >= 0; i--) {
            T element = super.get(i);
            if (element.getState() == state) {
                return element;
            }
        }
        return null;
    }
}
