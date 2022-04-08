package org.github.palace.bot.core.cli.support;

/**
 * @author jihongyuan
 * @date 2022/4/8 9:38
 */
public class CommandManagerFactory {

    private static CommandManager commandManager;

    public static void setCommandManager(CommandManager commandManager) {
        CommandManagerFactory.commandManager = commandManager;
    }

    public static CommandManager instance() {
        return commandManager;
    }

}
