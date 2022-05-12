package org.github.palace.bot.core.exception;

/**
 * @author jihongyuan
 * @date 2022/5/9 16:38
 */
public class ParameterResolverException extends BotException {

    public ParameterResolverException(String message) {
        super(message);
    }

    public ParameterResolverException(String message, Throwable cause) {
        super(message, cause);
    }

    public ParameterResolverException(Throwable cause) {
        super(cause);
    }
}
