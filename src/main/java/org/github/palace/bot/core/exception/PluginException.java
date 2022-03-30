package org.github.palace.bot.core.exception;

/**
 * @author JHY
 * @date 2022/3/30 22:07
 */
public class PluginException extends BotException {

    public PluginException(String message) {
        super(message);
    }

    public PluginException(String message, Throwable cause) {
        super(message, cause);
    }

    public PluginException(Throwable cause) {
        super(cause);
    }

}
