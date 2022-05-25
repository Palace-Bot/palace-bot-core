package org.github.palace.bot.core.plugin;

import net.mamoe.mirai.contact.MemberPermission;
import org.github.palace.bot.core.LifeCycle;
import org.github.palace.bot.core.cli.CommandSender;
import org.github.palace.bot.core.cli.CommandSession;

/**
 * @author jihongyuan
 * @date 2022/5/12 15:42
 */
public interface PluginManager extends LifeCycle {

    /**
     * Loader plugins
     */
    void load();

    /**
     * 执行具体命令
     *
     * @param commandSender 命令发送者
     * @param command       命令
     */
    void executeCommand(CommandSender commandSender, AbstractCommand command);

    /**
     * 执行具体命令
     *
     * @param commandSender 命令发送者
     * @param command       命令
     * @param session       子命令，父命令session, 否则当前命令session
     */
    void executeCommand(CommandSender commandSender, AbstractCommand command, CommandSession session);

    /**
     * 匹配命令
     *
     * @param commandName 命令名称（不加前缀 如：/）
     * @return command
     */
    AbstractCommand matchCommand(String commandName,MemberPermission permission);


    /**
     * 匹配子命令
     *
     * @param commandName 命令名称（不加前缀 如：/）
     * @return command
     */
    AbstractCommand matchCommand(String commandName, AbstractCommand command, MemberPermission permission);

}
