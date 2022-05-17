package org.github.palace.bot.core.cli;

import lombok.AllArgsConstructor;
import lombok.Data;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.contact.User;
import net.mamoe.mirai.event.Event;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.Message;
import net.mamoe.mirai.message.data.MessageChain;
import org.github.palace.bot.core.cli.support.MemberCommandSender;
import org.jetbrains.annotations.Nullable;

/**
 * @author JHY
 * @date 2022/4/2 14:37
 */
@Data
@AllArgsConstructor
public abstract class CommandSender {

    private Bot bot;

    private Contact subject;

    private User user;

    /**
     * 发送者名称
     */
    private String name;

    private MessageChain messageChain;

    /**
     * 立刻发送一条消息.
     * <p>
     * 对于 [MemberCommandSender], 这个函数总是发送给所在群
     */
    public abstract void sendMessage(Message message);

    /**
     * 立刻发送一条消息.
     * <p>
     * 对于 [MemberCommandSender], 这个函数总是发送给所在群
     */
    public abstract void sendMessage(String message);

    @Nullable
    public static CommandSender toCommandSender(Event event) {
        if (event instanceof GroupMessageEvent) {
            return MemberCommandSender.toCommandSender((GroupMessageEvent) event);
        }
        return null;
    }

}
