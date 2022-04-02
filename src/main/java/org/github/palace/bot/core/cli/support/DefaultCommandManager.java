package org.github.palace.bot.core.cli.support;

import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.SingleMessage;
import org.github.palace.bot.core.cli.AbstractCommand;
import org.github.palace.bot.core.cli.CommandSender;
import org.github.palace.bot.core.plugin.Plugin;
import org.github.palace.bot.core.plugin.PluginLoader;
import org.github.palace.bot.core.plugin.PluginRegister;

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
    private final String commandPrefix;

    private final PluginRegister pluginRegister;

    public DefaultCommandManager(String commandPrefix) {
        this.commandPrefix = commandPrefix;
        this.pluginRegister = new PluginRegister();
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
