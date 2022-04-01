package org.github.palace.bot.core.cli.support;

import net.mamoe.mirai.message.data.MessageChain;
import org.github.palace.bot.core.cli.AbstractCommand;
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
    public void executeCommand(AbstractCommand command, MessageChain arguments) {

    }


    @Override
    public AbstractCommand matchCommand(String commandName) {
        if (commandName.startsWith(commandPrefix) && commandName.length() > 1) {
            List<AbstractCommand> matchList = new ArrayList<>();

            Map<PluginLoader, Plugin> pluginCache = pluginRegister.getPluginCache();
            for (Map.Entry<PluginLoader, Plugin> entry : pluginCache.entrySet()) {
                Plugin plugin = entry.getValue();
                for (AbstractCommand command : plugin.getCommands()) {
                    if (commandName.equals(command.getPrimaryName())) {
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
