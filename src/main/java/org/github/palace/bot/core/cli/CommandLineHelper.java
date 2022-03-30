package org.github.palace.bot.core.cli;

import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.MessageSource;
import net.mamoe.mirai.message.data.SingleMessage;
import org.github.palace.bot.core.collection.GroupContextMap;

/**
 * 命令行目前命令只针对群组
 *
 * @author JHY
 * @date 2022/3/28 9:02
 */
public class CommandLineHelper {

    // 我也不懂是不是线程安全
    /**
     * 保留用户命令上下文
     * key: 群号, value: {key: qq, value: 最近聊天记录}
     */
    private final GroupContextMap groupContextMap = new GroupContextMap();

    public CommandLine put(MessageSource messageSource, Command command) {
        CommandLine commandLine = new CommandLine(command).runnable();
        groupContextMap.put(messageSource.getTargetId(), messageSource.getFromId(), commandLine);
        return commandLine;
    }

    public CommandLine get(MessageSource messageSource, CommandLine.State state) {
        return groupContextMap.get(messageSource.getTargetId(), messageSource.getFromId(), state);
    }

    public CommandLine prepare(CommandLine commandLine) {
        return commandLine.prepare();
    }

    public CommandLine crash(CommandLine commandLine) {
        return commandLine.crash();
    }

    public CommandLine finish(CommandLine commandLine) {
        return commandLine.finish();
    }

    public void sendDetermine(Group subject, MessageSource messageSource, SingleMessage singleMessage) {
        String determine = singleMessage.contentToString().trim().toLowerCase();

        At at = new At(messageSource.getFromId());
        CommandLine commandLine = this.get(messageSource, CommandLine.State.PREPARE);

        if ("y".equals(determine) || "yes".equals(determine)) {
            subject.sendMessage(at.plus(" ").plus(commandLine.getCommand().getPrimaryName()).plus(" 命令正在处理中..."));
            subject.sendMessage(at.plus(" ").plus(commandLine.getCommand().getPrimaryName()).plus(" 已结束..."));
        } else if ("n".equals(determine) || "no".equals(determine)) {
            subject.sendMessage(at.plus(" ").plus(commandLine.getCommand().getPrimaryName()).plus(" 命令已拒绝"));
        }
    }
}
