package org.github.palace.bot.core.plugin;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.contact.MemberPermission;
import org.github.palace.bot.core.cli.AbstractCommand;
import org.github.palace.bot.core.cli.CommandSender;
import org.github.palace.bot.core.cli.CommandSession;
import org.github.palace.bot.core.loader.PluginClassLoader;
import org.github.palace.bot.utils.ZipUtil;

import java.io.File;
import java.util.*;


/**
 * 插件式类加载器
 * 一个jar包一个加载器做隔离
 *
 * @author JHY
 * @date 2022/3/30 16:07
 */
@Slf4j
public abstract class AbstractPluginManager implements PluginManager {

    /**
     * 插件目录
     */
    protected final String pluginPath;

    /**
     * 命令管理器
     */
    @Setter
    protected CommandManager commandManager;

    /**
     * 插件集合
     */
    protected final List<PluginWrapper> plugins;

    public AbstractPluginManager(String pluginPath, CommandManager commandManager) {
        this.pluginPath = pluginPath;
        this.commandManager = commandManager;
        this.plugins = new ArrayList<>();
    }

    @Override
    public void load() {
        File[] directoryChildFiles = ZipUtil.getDirectoryChildFiles(pluginPath);

        for (File file : directoryChildFiles) {
            try {
                // (1) 解压jar包
                String path = file.getPath();
                if (file.getName().endsWith(".jar")) {
                    path = path.replace(".jar", "");
                    ZipUtil.unzip(file, path);
                }

                // (2) 创建类加载器
                PluginClassLoader pluginClassLoader = new PluginClassLoader(path);

                // (3) TODO 解析jar包中配置文件
                PluginProperties pluginProperties = new PluginProperties(pluginClassLoader);

                // (4) 创建插件包装器
                PluginWrapper pluginWrapper = new PluginWrapper(pluginProperties, pluginClassLoader);

                plugins.add(pluginWrapper);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }

    }

    @Override
    public void executeCommand(CommandSender commandSender, AbstractCommand command, MemberPermission permission) {
        executeCommand(commandSender, command, permission, null);
    }


    @Override
    public void executeCommand(CommandSender commandSender, AbstractCommand command, MemberPermission permission, CommandSession session) {
        commandManager.executeCommand(commandSender, command, permission, session);
    }

    @Override
    public AbstractCommand matchCommand(String commandName) {
        if (commandName.startsWith(commandManager.getCommandPrefix()) && commandName.length() > 1) {
            for (PluginWrapper pluginWrapper : plugins) {
                AbstractCommand command = commandManager.matchCommand(pluginWrapper, commandName);
                if (command != null) {
                    return command;
                }
            }
        }
        return null;
    }

    @Override
    public AbstractCommand matchCommand(String commandName, AbstractCommand command) {
        return commandManager.matchCommand(commandName, command);
    }
}