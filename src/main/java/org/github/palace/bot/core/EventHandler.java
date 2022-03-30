package org.github.palace.bot.core;

import net.mamoe.mirai.event.Event;

/**
 * @author JHY
 * @date 2022/3/22 16:47
 */
@FunctionalInterface
public interface EventHandler<E extends Event> {

    /**
     * 处理来自 {@code mirai} 事件
     *
     * @param event mirai event
     */
    void onEvent(E event);

    /**
     * 获取处理器支持的event
     *
     * @return event class
     */
    default Class<E> getHandlerEvent() {
        throw new RuntimeException("handler is not null");
    }

}
