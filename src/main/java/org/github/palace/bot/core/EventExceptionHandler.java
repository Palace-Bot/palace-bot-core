package org.github.palace.bot.core;

import kotlin.coroutines.AbstractCoroutineContextElement;
import kotlin.coroutines.CoroutineContext;
import kotlinx.coroutines.CoroutineExceptionHandler;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

/**
 * @author JHY
 * @date 2022/3/29 20:32
 */
public class EventExceptionHandler extends AbstractCoroutineContextElement implements CoroutineExceptionHandler {
    private final Logger logger;

    public EventExceptionHandler(Logger logger) {
        super(CoroutineExceptionHandler.Key);
        this.logger = logger;
    }

    @Override
    public void handleException(@NotNull CoroutineContext coroutineContext, @NotNull Throwable throwable) {
        throwable.printStackTrace();
        logger.error(throwable.getMessage());
    }

}
