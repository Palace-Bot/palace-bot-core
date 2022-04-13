package org.github.palace.bot.core.collection;


import org.github.palace.bot.core.cli.CommandSession;

import java.util.*;

/**
 * @author JHY
 * @date 2022/3/25 7:27
 */
public final class MemberContextStack<T extends CommandSession> extends LinkedList<T> {
    private final int initialCapacity;

    public MemberContextStack(int initialCapacity) {
        this.initialCapacity = initialCapacity;
    }

    @Override
    public boolean add(T t) {
        // 保证一般情况下最大容量不会超过 initialCapacity
        if (size() == initialCapacity) {
            // 栈底如果不存在未处理弹出
            T item = get(0);
            if (item.getState() != CommandSession.State.PREPARE) {
                poll();
            }
        }
        return super.add(t);
    }

    /**
     * 从栈顶寻找 {@code status} 的第一条记录
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

    /**
     * 从栈底寻找 {@code status} 的第一条记录
     *
     * @param state 状态
     * @return element
     */
    public T getLast(CommandSession.State state) {
        for (T element : this) {
            if (element.getState() == state) {
                return element;
            }
        }
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        MemberContextStack<?> that = (MemberContextStack<?>) o;
        return initialCapacity == that.initialCapacity;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), initialCapacity);
    }

}
