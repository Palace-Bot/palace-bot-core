package org.github.palace.bot.core.plugin;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * 插件主类
 *
 * @author JHY
 * @date 2022/3/30 15:47
 */
@Getter
@Setter
public class Plugin {

    protected List<AbstractCommand> commands = new ArrayList<>();

    /**
     * 插件版本
     */
    protected String version;

    /**
     * 插件名称
     */
    protected String name;

    /**
     * 插件说明
     */
    protected String description;

    protected String[] scanBasePackages;

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