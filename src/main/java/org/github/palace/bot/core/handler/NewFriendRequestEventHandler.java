package org.github.palace.bot.core.handler;

import net.mamoe.mirai.event.events.NewFriendRequestEvent;
import org.github.palace.bot.core.EventHandler;

/**
 * @author JHY
 * @date 2022/3/23 16:38
 */
public class NewFriendRequestEventHandler implements EventHandler<NewFriendRequestEvent> {

    @Override
    public void onEvent(NewFriendRequestEvent event) {
        event.accept();
    }

    @Override
    public Class<NewFriendRequestEvent> getHandlerEvent() {
        return NewFriendRequestEvent.class;
    }
}
