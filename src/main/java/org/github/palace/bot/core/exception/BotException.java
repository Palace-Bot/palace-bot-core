package org.github.palace.bot.core.exception;

/**
 * @author JHY
 * @date 2022/3/30 22:08
 */
public class BotException extends RuntimeException{

    public BotException(String message) {
        super(message);
    }

    public BotException(String message, Throwable cause) {
        super(message, cause);
    }

    public BotException(Throwable cause) {
        super(cause);
    }

}
