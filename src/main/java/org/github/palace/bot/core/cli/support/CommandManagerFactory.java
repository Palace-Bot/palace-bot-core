package org.github.palace.bot.core.cli.support;

/**
 * @author jihongyuan
 * @date 2022/4/8 9:38
 */
public final class CommandManagerFactory {

    private static CommandManager commandManager;

    private CommandManagerFactory() {
    }

    public static void setCommandManager(CommandManager commandManager) {
        CommandManagerFactory.commandManager = commandManager;
    }

    public static CommandManager instance() {
        return commandManager;
    }

}
