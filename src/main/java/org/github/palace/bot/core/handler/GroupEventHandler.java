package org.github.palace.bot.core.handler;

import org.github.palace.bot.core.EventHandler;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.*;
import org.github.palace.bot.core.cli.AbstractCommand;
import org.github.palace.bot.core.cli.CommandSession;
import org.github.palace.bot.core.cli.support.CommandSessionHelper;
import org.github.palace.bot.core.cli.support.CommandManager;
import org.github.palace.bot.core.cli.support.DefaultCommandManager;
import org.github.palace.bot.core.utils.MiraiCodeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author JHY
 * @date 2022/3/22 15:36
 */
public class GroupEventHandler implements EventHandler<GroupMessageEvent> {
    private static final Logger LOGGER = LoggerFactory.getLogger(GroupEventHandler.class);

    private final CommandSessionHelper commandSessionHelper = new CommandSessionHelper();
    private final CommandManager commandManager = new DefaultCommandManager("/");

    @Override
    public void onEvent(GroupMessageEvent event) {
        Group subject = event.getSubject();
        MessageChain chain = event.getMessage();

        MessageSource messageSource = (MessageSource) chain.get(0);
        String miraiCode = chain.serializeToMiraiCode();

        // at机器人 默认 at后面就是命令
        if (MiraiCodeUtil.isAtMe(miraiCode)) {
            AbstractCommand command = commandManager.matchCommand(chain.get(2).contentToString().trim());
            if (command != null) {
                CommandSession commandSession = commandSessionHelper.put(messageSource, command);

                Exception exception = null;
                try {
                    commandManager.executeCommand(command, chain);
                } catch (Exception e) {
                    exception = e;
                }

                if (exception == null) {
                    if (!command.isDetermine()) {
                        commandSessionHelper.finish(commandSession);
                    } else {  // 重复确定
                        subject.sendMessage(new At(messageSource.getFromId()).plus(" Is this ok [Y/n]:"));
                        commandSessionHelper.prepare(commandSession);
                    }
                } else {
                    // TODO 异常发送给某人, 还没想好怎么做
                    commandSessionHelper.crash(commandSession);
                    LOGGER.error(exception.getMessage());
                }
            }
            return;
        }

        CommandSession commandSession = commandSessionHelper.get(messageSource, CommandSession.State.PREPARE);
        // 存在待处理命令
        if (commandSession != null) {
            commandSessionHelper.sendDetermine(subject, messageSource, chain.get(1));
            commandSessionHelper.finish(commandSession);
        }
    }

    @Override
    public Class<GroupMessageEvent> getHandlerEvent() {
        return GroupMessageEvent.class;
    }

}
