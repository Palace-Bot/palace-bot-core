package org.github.palace.bot.core.plugin;

import lombok.Getter;
import org.github.palace.bot.core.cli.AbstractCommand;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * 插件主类
 *
 * @author JHY
 * @date 2022/3/30 15:47
 */
public class Plugin {

    @Getter
    protected Set<AbstractCommand> commands = new HashSet<>();

    @Getter
    private ScheduledExecutorService pusherExecutorService = Executors.newSingleThreadScheduledExecutor();

    @Getter
    private String version;

    @Getter
    private String name;

    @Getter
    private String description;

    protected Plugin() {
    }

    protected Plugin(String version, String name, String description) {
        this.version = version;
        this.name = name;
        this.description = description;
    }

    public void onLoad() {
    }

    public void onEnable() {
    }

    public void onDisable() {
    }

    /**
     * 注册命令
     */
    protected Plugin register(AbstractCommand command) {
        command.parse();
        commands.add(command);
        return this;
    }

}