package org.github.palace.bot.core.handler;

import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.Friend;
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
import org.github.palace.bot.core.constant.BaseConstant;
import org.github.palace.bot.core.utils.MiraiCodeUtil;
import org.github.palace.bot.data.message.entity.MessageDO;
import org.github.palace.bot.data.message.service.MessageService;
import org.github.palace.bot.data.message.service.impl.MessageServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
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

        CommandSession prepareCommandSession = commandSessionHelper.get(messageSource, CommandSession.State.PREPARE);
        // 尝试处理determine
        if (prepareCommandSession != null && commandSessionHelper.trySendDetermine(subject, messageSource, chain.get(1))) {
            commandSessionHelper.finish(prepareCommandSession);
            return;
        }

        AbstractCommand command;
        Exception exception = null;

        // at机器人 默认 at后面就是命令
        if (MiraiCodeUtil.isAtMe(chain.serializeToMiraiCode(), subject.getBot().getId())) {
            command = commandManager.matchCommand(chain.get(2).contentToString().trim());
        } else if (prepareCommandSession != null) {
            command = commandManager.matchCommand(chain.get(1).contentToString().trim(), prepareCommandSession.getCommand());
        } else {
            command = commandManager.matchCommand(chain.get(1).contentToString().trim());
        }

        if (command != null) {
            CommandSession commandSession = commandSessionHelper.put(messageSource, command, chain);
            try {
                // 处理子命令
                if (prepareCommandSession != null) {
                    commandManager.executeCommand(CommandSender.toCommandSender(event), commandSession.getCommand(), prepareCommandSession, chain);
                } else {
                    commandManager.executeCommand(CommandSender.toCommandSender(event), command, chain);
                }
            } catch (Exception e) {
                exception = e;
            }

            if (exception == null) {
                // 重复确定
                if (command.isDetermine()) {
                    subject.sendMessage(new At(messageSource.getFromId()).plus(" Is this ok [Y/n]:"));
                    commandSessionHelper.prepare(commandSession);
                }
                // 存在命令
                else if (command.hasChildrenCommand()) {
                    commandSessionHelper.prepare(commandSession);
                } else {
                    commandSessionHelper.finish(commandSession);
                }
            } else {
                // 异常发送给使用者
                Bot bot = event.getBot();
                Friend friend = bot.getFriend(BaseConstant.USER);
                if (friend != null) {
                    String stackTrace = Arrays.stream(exception.getStackTrace()).map(StackTraceElement::toString).collect(Collectors.joining("\r\n"));
                    friend.sendMessage(exception.getMessage() + "\r\n" + stackTrace);
                }
                commandSessionHelper.crash(commandSession);
                LOGGER.error(exception.getMessage(), exception);
            }
            return;
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
