package org.github.palace.bot.core.cli.support;

import net.mamoe.mirai.message.data.MessageChain;
import org.github.palace.bot.core.cli.AbstractCommand;
import org.github.palace.bot.core.cli.CommandSender;

/**
 * 命令管理接口
 *
 * @author JHY
 * @date 2022/3/27 9:43
 */
public interface CommandManager {

    /**
     * TODO 目前不支持手动注册命令, 必须已经插件模式注册
     *
     * @param command command
     * @return register status
     */
    boolean registerCommand(AbstractCommand command);

    /**
     * 执行具体命令
     *
     * @param commandSender 命令发送者
     * @param command       命令
     * @param chain         消息链
     */
    void executeCommand(CommandSender commandSender, AbstractCommand command, MessageChain chain);

    /**
     * 启动主动推送
     */
    void startCommandPush();

    /**
     * 停止主动推送
     */
    void stopCommandPush();

    /**
     * 匹配命令
     *
     * @param commandName 命令名称（不加前缀 如：/）
     * @return command
     */
    AbstractCommand matchCommand(String commandName);

}
