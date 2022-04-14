package org.github.palace.bot.core.cli.support;

import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.MemberPermission;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.SingleMessage;
import org.github.palace.bot.core.annotation.CommandPusher;
import org.github.palace.bot.core.cli.AbstractCommand;
import org.github.palace.bot.core.cli.CommandSender;
import org.github.palace.bot.core.cli.CommandSession;
import org.github.palace.bot.core.plugin.Plugin;
import org.github.palace.bot.core.plugin.PluginLoader;
import org.github.palace.bot.core.plugin.PluginRegister;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * 全局命令管理器，用于执行命令
 *
 * @author JHY
 * @date 2022/3/27 15:28
 */
public class DefaultCommandManager implements CommandManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultCommandManager.class);

    /**
     * 命令前缀
     */
    private final String commandPrefix;

    /**
     * 插件注册器
     */
    private final PluginRegister pluginRegister;

    /**
     * 机器人信息
     */
    private final Bot bot;

    public DefaultCommandManager(String commandPrefix, Bot bot) {
        this.commandPrefix = commandPrefix;
        this.bot = bot;
        this.pluginRegister = new PluginRegister();
        // 插件加载
        pluginRegister.init();
    }

    @Override
    public boolean registerCommand(AbstractCommand command) {
        return false;
    }

    @Override
    public void executeCommand(CommandSender commandSender, AbstractCommand command, MemberPermission permission, MessageChain chain) {
        this.executeCommand(commandSender, command, permission, null, chain);
    }

    @Override
    public void executeCommand(CommandSender commandSender, AbstractCommand command, MemberPermission permission, CommandSession session, MessageChain chain) {
        // TODO 待优化
        Object[] args = new Object[6 + chain.size()];

        args[0] = commandSender;
        args[1] = commandSender.getBot();
        args[2] = commandSender.getSubject();
        args[3] = commandSender.getUser();
        args[4] = commandSender.getName();
        args[5] = session;
        int current = 6;
        for (SingleMessage singleMessage : chain) {
            args[current++] = singleMessage;
        }

        InvocableMethod invocableMethod = ParameterMapping.mapping(permission, command, args);
        if (invocableMethod != null) {
            invocableMethod.doInvoke();
        }
    }

    @Override
    public void startCommandPush() {
        Map<PluginLoader, Plugin> pluginCache = pluginRegister.getPluginCache();
        for (Map.Entry<PluginLoader, Plugin> entry : pluginCache.entrySet()) {
            Plugin plugin = entry.getValue();
            for (AbstractCommand command : plugin.getCommands()) {
                Map<Method, CommandPusher> commandPushMethodMap = command.getCommandPusherMethodMap();
                commandPushMethodMap.forEach((method, commandPusher) -> plugin.getPusherExecutorService().scheduleAtFixedRate(() -> {
                    try {
                        method.invoke(command, bot.getGroups());
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        LOGGER.error(e.getMessage(), e);
                    }
                }, 0, commandPusher.value(), commandPusher.unit()));

            }
        }
    }

    @Override
    public void stopCommandPush() {
        Map<PluginLoader, Plugin> pluginCache = pluginRegister.getPluginCache();
        for (Map.Entry<PluginLoader, Plugin> entry : pluginCache.entrySet()) {
            Plugin plugin = entry.getValue();
            plugin.getPusherExecutorService().shutdown();
        }
    }

    @Nullable
    @Override
    public AbstractCommand matchCommand(String commandName) {
        if (commandName.startsWith(commandPrefix) && commandName.length() > 1) {
            Map<PluginLoader, Plugin> pluginCache = pluginRegister.getPluginCache();
            for (Map.Entry<PluginLoader, Plugin> entry : pluginCache.entrySet()) {
                Plugin plugin = entry.getValue();
                for (AbstractCommand command : plugin.getCommands()) {
                    if (commandName.equals(commandPrefix + command.getPrimaryName())) {
                        return command;
                    }
                }
            }
        }
        return null;
    }

    @Nullable
    @Override
    public AbstractCommand matchCommand(String commandName, AbstractCommand command) {
        for (AbstractCommand abstractCommand : command.getChildrenCommand()) {
            if (commandName.equals(abstractCommand.getPrimaryName()) || "*".equals(abstractCommand.getPrimaryName())) {
                return abstractCommand;
            }
        }
        return null;
    }

}
