package org.github.palace.bot.core.cli;

/**
 * @author JHY3
 * @date 2022/3/31 15:44
 */
public abstract class AbstractCommand extends Command{

    protected AbstractCommand(String primaryName, Void permission, boolean determine, String description) {
        super(primaryName, permission, determine, description);
    }

}