package org.github.palace.bot.core.plugin;

import lombok.experimental.Delegate;
import org.github.palace.bot.core.annotation.Command;
import org.github.palace.bot.core.exception.PluginException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author jihongyuan
 * @date 2022/9/1 15:15
 */
public class CommandDelegate extends AbstractCommand {

    private final Class<?> delegateClass;

    /**
     * 代理类
     */
    @Delegate
    private final Object delegate;

    protected CommandDelegate(Command command, Class<?> delegateClass) {
        super(command.primaryName(), command.permission(), command.scope(), command.determine(), command.description());
        this.delegateClass = delegateClass;

        try {
            this.delegate = delegateClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new PluginException(e);
        }

    }

    @Override
    public void parse() {
        Method[] methods = delegateClass.getDeclaredMethods();
        if (methods.length > 0) {
            for (Method method : methods) {
                parse(method);
            }
        }
    }

    public Object invoke(Method method, Object[] args) throws InvocationTargetException, IllegalAccessException {
        return method.invoke(delegate, args);
    }

}
