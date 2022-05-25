package org.github.palace.bot.core.plugin;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.contact.MemberPermission;
import org.github.palace.bot.core.cli.CommandSender;
import org.github.palace.bot.core.cli.CommandSession;
import org.github.palace.bot.core.loader.PluginClassLoader;
import org.github.palace.bot.utils.ZipUtil;

import javax.annotation.Nullable;
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
    protected CommandExecutor commandExecutor = new CommandExecutor();

    /**
     * 插件集合
     */
    protected final List<PluginWrapper> plugins;

    protected AbstractPluginManager(String pluginPath) {
        this.pluginPath = pluginPath;
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

                long start = System.currentTimeMillis();

                // (2) 创建类加载器
                PluginClassLoader pluginClassLoader = new PluginClassLoader(path);

                // (3) TODO 解析jar包中配置文件
                PluginProperties pluginProperties = new PluginProperties(pluginClassLoader);

                // (4) 创建插件包装器
                PluginWrapper pluginWrapper = new PluginWrapper(pluginProperties, pluginClassLoader);

                log.info("load plugin: {} time: {}ms", path, System.currentTimeMillis() - start);
                plugins.add(pluginWrapper);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }

    }

    @Override
    public void executeCommand(CommandSender commandSender, AbstractCommand command) {
        executeCommand(commandSender, command, null);
    }


    @Override
    public void executeCommand(CommandSender commandSender, AbstractCommand command, CommandSession session) {
        commandExecutor.executeCommand(commandSender, command, session);
    }

    @Nullable
    @Override
    public AbstractCommand matchCommand(String commandName, MemberPermission permission) {
        if (commandName.length() < 1) {
            return null;
        }

        AbstractCommand command = null;
        // TODO 同名命令 存在权限优先级问题
        for (PluginWrapper pluginWrapper : plugins) {
            if (
                    commandName.startsWith(pluginWrapper.getProperties().commandPrefix) &&
                    (command = pluginWrapper.getCommandManager().matchCommand(commandName)) != null &&
                    permission.compareTo(command.getPermission()) >= 0) {
                break;
            }

        }
        return command;
    }

    @Override
    public AbstractCommand matchCommand(String commandName, AbstractCommand command, MemberPermission permission) {
        return commandExecutor.matchCommand(commandName, command);
    }

}