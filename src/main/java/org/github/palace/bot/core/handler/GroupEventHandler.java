package org.github.palace.bot.core.handler;

import org.github.palace.bot.core.EventHandler;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.*;
import org.github.palace.bot.core.cli.AbstractCommand;
import org.github.palace.bot.core.cli.CommandSender;
import org.github.palace.bot.core.cli.CommandSession;
import org.github.palace.bot.core.cli.support.CommandManagerFactory;
import org.github.palace.bot.core.cli.support.CommandSessionHelper;
import org.github.palace.bot.core.cli.support.CommandManager;
import org.github.palace.bot.core.utils.MiraiCodeUtil;
import org.github.palace.bot.data.message.entity.MessageDO;
import org.github.palace.bot.data.message.service.MessageService;
import org.github.palace.bot.data.message.service.impl.MessageServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.stream.Collectors;

/**
 * @author JHY
 * @date 2022/3/22 15:36
 */
public class GroupEventHandler implements EventHandler<GroupMessageEvent> {
    private static final Logger LOGGER = LoggerFactory.getLogger(GroupEventHandler.class);

    private final CommandSessionHelper commandSessionHelper = new CommandSessionHelper();
    private final CommandManager commandManager = CommandManagerFactory.instance();

    @Override
    public void onEvent(GroupMessageEvent event) {
        Group subject = event.getSubject();
        MessageChain chain = event.getMessage();

        MessageSource messageSource = (MessageSource) chain.get(0);
        String miraiCode = chain.serializeToMiraiCode();

        // at机器人 默认 at后面就是命令
        if (MiraiCodeUtil.isAtMe(miraiCode, subject.getBot().getId())) {
            AbstractCommand command = commandManager.matchCommand(chain.get(2).contentToString().trim());
            if (command != null) {
                CommandSession commandSession = commandSessionHelper.put(messageSource, command);

                Exception exception = null;
                try {
                    commandManager.executeCommand(CommandSender.toCommandSender(event), command, chain);
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
                    LOGGER.error(exception.getMessage(), exception);
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

        // data
        MessageService messageService = new MessageServiceImpl();
        // 保存聊天记录
        messageService.save(subject.getId(), MessageDO.builder()
                .memberId(messageSource.getFromId())
                .message(chain.stream().map(SingleMessage::contentToString).collect(Collectors.joining()))
                .createAt(new Date()).build());
    }

    @Override
    public Class<GroupMessageEvent> getHandlerEvent() {
        return GroupMessageEvent.class;
    }

}
