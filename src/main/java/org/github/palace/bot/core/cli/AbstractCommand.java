package org.github.palace.bot.core.cli;

import lombok.Getter;
import org.github.palace.bot.core.annotation.CommandHandler;
import org.github.palace.bot.core.annotation.CommandPusher;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author JHY3
 * @date 2022/3/31 15:44
 */
public abstract class AbstractCommand extends Command {

    /**
     * 命令处理器
     */
    @Getter
    private final Map<Method, CommandHandler> commandHandlerMethodMap = new HashMap<>(16);

    /**
     * 推送器
     */
    @Getter
    private final Map<Method, CommandPusher> commandPushMethodMap = new HashMap<>(16);

    protected AbstractCommand(String primaryName, Void permission, boolean determine, String description) {
        super(primaryName, permission, determine, description);
    }

    /**
     * 解析 命令处理器和推送器
     */
    public void parse() {
        Method[] methods = this.getClass().getDeclaredMethods();
        if (methods.length > 0) {
            for (Method method : methods) {
                if (method.isAnnotationPresent(CommandHandler.class)) {
                    commandHandlerMethodMap.put(method, method.getAnnotation(CommandHandler.class));
                } else if (method.isAnnotationPresent(CommandPusher.class)) {
                    commandPushMethodMap.put(method, method.getAnnotation(CommandPusher.class));
                }
            }
        }
    }

}