package org.github.palace.bot.core.plugin;


import org.github.palace.bot.core.annotation.Application;
import org.github.palace.bot.core.annotation.OnDisable;
import org.github.palace.bot.core.annotation.OnEnable;
import org.github.palace.bot.core.annotation.OnLoad;
import org.github.palace.bot.core.exception.PluginException;

import java.lang.reflect.Method;

/**
 * @author jihongyuan
 * @date 2022/6/1 16:40
 */
public class PluginDelegate extends Plugin {

    private Class<?> delegateClass;

    /**
     * 代理类
     */
    private final Object delegate;

    private Method onLoad;

    private Method onEnable;

    private Method onDisable;

    public PluginDelegate(Application application, Class<?> delegateClass) {
        validate(application);

        super.setVersion(application.version());
        super.setName(application.name());
        super.setDescription(application.description());

        this.delegateClass = delegateClass;
        try {
            this.delegate = delegateClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new PluginException(e);
        }

        Method[] methods = delegateClass.getDeclaredMethods();
        for (Method method : methods) {
            Class<?>[] parameterTypes = method.getParameterTypes();
            if (parameterTypes.length == 0) {
                if (method.isAnnotationPresent(OnLoad.class)) {
                    onLoad = method;
                }
                if (method.isAnnotationPresent(OnEnable.class)) {
                    onEnable = method;
                }
                if (method.isAnnotationPresent(OnDisable.class)) {
                    onDisable = method;
                }
            }
        }
    }

    private void validate(Application application) {
        assert application.version() != null;
        assert application.name() != null;
        assert application.description() != null;
    }

    @Override
    public void onLoad() {
        if (onLoad != null) {
            try {
                onLoad.invoke(delegate);
            } catch (Exception e) {
                throw new PluginException(e);
            }
        }
    }

    @Override
    public void onEnable() {
        if (onEnable != null) {
            try {
                onEnable.invoke(delegate);
            } catch (Exception e) {
                throw new PluginException(e);
            }
        }
    }

    @Override
    public void onDisable() {
        if (onDisable != null) {
            try {
                onDisable.invoke(delegate);
            } catch (Exception e) {
                throw new PluginException(e);
            }
        }
    }

}
