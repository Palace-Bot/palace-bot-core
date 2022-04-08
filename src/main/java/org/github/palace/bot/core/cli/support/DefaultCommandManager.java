package org.github.palace.bot.core.cli.support;

import net.mamoe.mirai.Bot;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.SingleMessage;
import org.github.palace.bot.core.annotation.CommandPusher;
import org.github.palace.bot.core.cli.AbstractCommand;
import org.github.palace.bot.core.cli.CommandSender;
import org.github.palace.bot.core.plugin.Plugin;
import org.github.palace.bot.core.plugin.PluginLoader;
import org.github.palace.bot.core.plugin.PluginRegister;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
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
    public void executeCommand(CommandSender commandSender, AbstractCommand command, MessageChain chain) {
        Object[] args = new Object[5 + chain.size()];

        args[0] = commandSender;
        args[1] = commandSender.getBot();
        args[2] = commandSender.getSubject();
        args[3] = commandSender.getUser();
        args[4] = commandSender.getName();

        int current = 5;
        for (SingleMessage singleMessage : chain) {
            args[current++] = singleMessage;
        }

        InvocableMethod invocableMethod = ParameterMapping.mapping(command, args);
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
                Map<Method, CommandPusher> commandPushMethodMap = command.getCommandPushMethodMap();
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


    @Override
    public AbstractCommand matchCommand(String commandName) {
        if (commandName.startsWith(commandPrefix) && commandName.length() > 1) {
            List<AbstractCommand> matchList = new ArrayList<>();

            Map<PluginLoader, Plugin> pluginCache = pluginRegister.getPluginCache();
            for (Map.Entry<PluginLoader, Plugin> entry : pluginCache.entrySet()) {
                Plugin plugin = entry.getValue();
                for (AbstractCommand command : plugin.getCommands()) {
                    if (commandName.equals(commandPrefix + command.getPrimaryName())) {
                        matchList.add(command);
                    }
                }
            }

            if (!matchList.isEmpty()) {
                return matchList.get(0);
            }
        }
        return null;
    }

}
