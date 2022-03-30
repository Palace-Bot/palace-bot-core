package org.github.palace.bot.core.cli.manager;

import net.mamoe.mirai.message.data.Message;
import net.mamoe.mirai.message.data.MessageSource;
import org.github.palace.bot.core.cli.Command;

/**
 * @author JHY
 * @date 2022/3/27 9:43
 */
public interface CommandManager {

    boolean registerCommand(Command command);

    void executeCommand(MessageSource messageSource, Message message);

    void executeCommand(MessageSource messageSource, Command command, Message arguments);

    Command matchCommand(String commandName);

}
