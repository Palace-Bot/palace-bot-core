package org.github.palace.bot.core.plugin;

import net.mamoe.mirai.contact.MemberPermission;
import org.github.palace.bot.core.cli.AbstractCommand;
import org.github.palace.bot.core.cli.CommandSender;
import org.github.palace.bot.core.cli.CommandSession;

/**
 * @author jihongyuan
 * @date 2022/5/12 15:42
 */
public interface PluginManager {

    /**
     * Loader plugins
     */
    void load();

    /**
     * Start plugin and command
     */
    void start();

    /**
     * Stop plugin and command
     */
    void stop();

    /**
     * 执行具体命令
     *
     * @param commandSender 命令发送者
     * @param command       命令
     * @param permission    用户权限
     */
    void executeCommand(CommandSender commandSender, AbstractCommand command, MemberPermission permission);

    /**
     * 执行具体命令
     *
     * @param commandSender 命令发送者
     * @param command       命令
     * @param permission    用户权限
     * @param session       子命令，父命令session, 否则当前命令session
     */
    void executeCommand(CommandSender commandSender, AbstractCommand command, MemberPermission permission, CommandSession session);

    /**
     * 匹配命令
     *
     * @param commandName 命令名称（不加前缀 如：/）
     * @return command
     */
    AbstractCommand matchCommand(String commandName);


    /**
     * 匹配子命令
     *
     * @param commandName 命令名称（不加前缀 如：/）
     * @return command
     */
    AbstractCommand matchCommand(String commandName, AbstractCommand command);

}
