package org.github.palace.bot.core.handler;

import org.github.palace.bot.core.EventHandler;
import net.mamoe.mirai.event.events.BotInvitedJoinGroupRequestEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 机器人被邀请加入群
 *
 * @author JHY
 * @date 2022/3/23 8:56
 */
public class BotInvitedJoinGroupRequestHandler implements EventHandler<BotInvitedJoinGroupRequestEvent> {
    private static final Logger LOGGER = LoggerFactory.getLogger(BotInvitedJoinGroupRequestHandler.class);

    /**
     * 默认，只要被要求默认就会加入群组
     */
    @Override
    public void onEvent(BotInvitedJoinGroupRequestEvent event) {
        event.accept();
        LOGGER.info("[Join Group Event] id: {} name: {}", event.getEventId(), event.getGroupName());
    }

    @Override
    public Class<BotInvitedJoinGroupRequestEvent> getHandlerEvent() {
        return BotInvitedJoinGroupRequestEvent.class;
    }

}
