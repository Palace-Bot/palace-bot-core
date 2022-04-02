package org.github.palace.bot.core.cli.support;

import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.Message;
import net.mamoe.mirai.message.data.PlainText;
import org.github.palace.bot.core.cli.CommandSender;

/**
 * 群组命令发送者
 *
 * @author JHY
 * @date 2022/4/2 14:47
 */
public class MemberCommandSender extends CommandSender {

    private MemberCommandSender(GroupMessageEvent event) {
        super(event.getBot(), event.getSubject(), event.getSender(), event.getSenderName());
    }

    @Override
    public void sendMessage(Message message) {
        getSubject().sendMessage(message);
    }

    @Override
    public void sendMessage(String message) {
        this.sendMessage(new PlainText(message));
    }

    public static CommandSender toCommandSender(GroupMessageEvent event) {
        return new MemberCommandSender(event);
    }

}
