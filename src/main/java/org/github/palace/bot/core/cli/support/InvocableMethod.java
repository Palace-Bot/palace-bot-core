package org.github.palace.bot.core.cli.support;

import org.github.palace.bot.core.exception.PluginException;
import org.github.palace.bot.core.plugin.CommandDelegate;

import java.lang.reflect.Method;

/**
 * @author JHY
 * @date 2022/4/2 17:11
 */
public class InvocableMethod {

    private final Object obj;
    private final Method method;
    private final Object[] args;

    public InvocableMethod(Object obj, Method method, Object[] args) {
        this.obj = obj;
        this.method = method;
        this.args = args;
    }

    public Object doInvoke() {
        try {
            if (obj instanceof CommandDelegate) {
                return ((CommandDelegate) obj).invoke(method, args);
            } else {
                return method.invoke(obj, args);
            }
        } catch (Exception e) {
            throw new PluginException(e);
        }
    }
}
