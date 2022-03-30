package org.github.palace.bot.core.handler;

import org.github.palace.bot.core.EventHandler;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.*;
import org.github.palace.bot.core.cli.Command;
import org.github.palace.bot.core.cli.CommandLine;
import org.github.palace.bot.core.cli.CommandLineHelper;
import org.github.palace.bot.core.cli.manager.CommandManager;
import org.github.palace.bot.core.cli.manager.DefaultCommandManager;
import org.github.palace.bot.core.utils.MiraiCodeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author JHY
 * @date 2022/3/22 15:36
 */
public class GroupEventHandler implements EventHandler<GroupMessageEvent> {
    private static final Logger LOGGER = LoggerFactory.getLogger(GroupEventHandler.class);

    private final CommandLineHelper commandLineHelper = new CommandLineHelper();
    private final CommandManager commandManager = new DefaultCommandManager("/");

    @Override
    public void onEvent(GroupMessageEvent event) {
        Group subject = event.getSubject();
        MessageChain chain = event.getMessage();

        MessageSource messageSource = (MessageSource) chain.get(0);

        String miraiCode = chain.serializeToMiraiCode();

        // at机器人
        if (MiraiCodeUtil.isAtMe(miraiCode)) {
            Command command = commandManager.matchCommand(chain.get(2).contentToString().trim());
            if (command != null) {
                CommandLine commandLine = commandLineHelper.put(messageSource, command);

                Exception exception = null;
                try {
                    commandManager.executeCommand(messageSource, command, chain);
                } catch (Exception e) {
                    exception = e;
                }

                if (exception == null) {
                    if (!command.isDetermine()) {
                        commandLineHelper.finish(commandLine);
                    } else {  // 重复确定
                        subject.sendMessage(new At(messageSource.getFromId()).plus(" Is this ok [Y/n]:"));
                        commandLineHelper.prepare(commandLine);
                    }
                } else {
                    // TODO 异常发送给某人, 还没想好怎么做
                    commandLineHelper.crash(commandLine);
                    LOGGER.error(exception.getMessage());
                }
            } else {
                // TODO 其他命令，考虑插件形式
                System.out.println();
            }
            return;
        }

        CommandLine commandLine = commandLineHelper.get(messageSource, CommandLine.State.PREPARE);
        // 存在待处理命令
        if (commandLine != null) {
            commandLineHelper.sendDetermine(subject, messageSource, chain.get(1));
            commandLineHelper.finish(commandLine);
        }
    }

    @Override
    public Class<GroupMessageEvent> getHandlerEvent() {
        return GroupMessageEvent.class;
    }

}
