package org.github.palace.bot.core.handler;

import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.Friend;
import net.mamoe.mirai.contact.NormalMember;
import org.github.palace.bot.core.EventHandler;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.*;
import org.github.palace.bot.core.plugin.AbstractCommand;
import org.github.palace.bot.core.cli.CommandSender;
import org.github.palace.bot.core.cli.CommandSession;
import org.github.palace.bot.core.cli.support.CommandSessionHelper;
import org.github.palace.bot.core.constant.BaseConstant;
import org.github.palace.bot.core.plugin.Loc;
import org.github.palace.bot.core.plugin.PluginManager;
import org.github.palace.bot.core.utils.MiraiCodeUtil;
import org.github.palace.bot.data.message.entity.MessageDO;
import org.github.palace.bot.data.message.service.MessageService;
import org.github.palace.bot.data.message.service.impl.MessageServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author JHY
 * @date 2022/3/22 15:36
 */
public class GroupEventHandler implements EventHandler<GroupMessageEvent> {
    private static final Logger LOGGER = LoggerFactory.getLogger(GroupEventHandler.class);

    private final CommandSessionHelper commandSessionHelper = new CommandSessionHelper();

    private final PluginManager pluginManager = Loc.get(PluginManager.class);

    // TODO 太烂了，准备重构
    @Override
    public void onEvent(GroupMessageEvent event) {
        Group subject = event.getSubject();

        MessageChain chain = event.getMessage();
        MessageSource messageSource = (MessageSource) chain.get(0);

        NormalMember member = Optional
                .ofNullable(subject.get(messageSource.getFromId()))
                .orElseThrow(() -> new RuntimeException("[Group Message Event] member is null"));


        // 获取待处理命令
        List<CommandSession> prepareCommandSessions = commandSessionHelper.get(messageSource, CommandSession.State.PREPARE);

        CommandSession prepareCommandSession = null;
        AbstractCommand command = null;
        Exception exception = null;

        // at机器人 默认 at后面就是命令
        if (MiraiCodeUtil.isAtMe(chain.serializeToMiraiCode(), subject.getBot().getId())) {
            command = pluginManager.matchCommand(chain.get(2).contentToString().trim(), member.getPermission());
        }
        // 上下文中存在确认命令
        else if (prepareCommandSessions != null && (prepareCommandSession = commandSessionHelper.trySendDetermine(subject, messageSource, chain.get(1))) != null) {
            command = prepareCommandSession.getCommand();
        }
        // 上下文中存在未处理命令, 匹配子命令
        else if (prepareCommandSessions != null) {
            for (CommandSession commandSession : prepareCommandSessions) {
                if ((command = pluginManager.matchCommand(chain.get(1).contentToString().trim(), commandSession.getCommand(), member.getPermission())) != null) {
                    prepareCommandSession = commandSession;
                    break;
                }
            }
        }
        // 普通命令
        else {
            command = pluginManager.matchCommand(chain.get(1).contentToString().trim(), member.getPermission());
        }

        if (command != null) {
            CommandSession commandSession = prepareCommandSession == null ? commandSessionHelper.put(messageSource, command, chain).runnable() : prepareCommandSession;
            try {
                // 等待确认
                if (prepareCommandSession == null && command.isDetermine()) {
                    subject.sendMessage(new At(member.getId()).plus(" Is this ok [Y/n]:"));
                    commandSessionHelper.prepare(commandSession);
                    return;
                }
                // 执行确认命令
                else if (prepareCommandSession != null && command.isDetermine()) {
                    pluginManager.executeCommand(CommandSender.toCommandSender(event), command, commandSession);
                }
                // 处理子命令
                else if (prepareCommandSession != null) {
                    pluginManager.executeCommand(CommandSender.toCommandSender(event), command, commandSession);
                } else {
                    pluginManager.executeCommand(CommandSender.toCommandSender(event), command, commandSession);
                }

            } catch (Exception e) {
                exception = e;
            }

            // 命令session处理
            if (exception == null) {
                // 存在命令
                if (command.hasChildrenCommand()) {
                    commandSessionHelper.prepare(commandSession);
                } else if (prepareCommandSessions == null) {
                    commandSessionHelper.finish(commandSession);
                }
            } else {
                // TODO 聊天显示不直观
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
                .memberId(member.getId())
                .message(chain.stream().map(SingleMessage::contentToString).collect(Collectors.joining()))
                .createAt(new Date()).build());
    }

    @Override
    public Class<GroupMessageEvent> getHandlerEvent() {
        return GroupMessageEvent.class;
    }

}
