package org.github.palace.bot.core.plugin;


import org.github.palace.bot.core.annotation.Application;
import org.github.palace.bot.core.annotation.OnDisable;
import org.github.palace.bot.core.annotation.OnEnable;
import org.github.palace.bot.core.annotation.OnLoad;
import org.github.palace.bot.core.exception.PluginException;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

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

    private final Map<Class<?>, Method> pluginLifeCycleMap = new HashMap<>(3);

    public PluginDelegate(Application application, Class<?> delegateClass) {
        validate(application);

        super.setVersion(application.version());
        super.setName(application.name());
        super.setDescription(application.description());
        super.setScanBasePackages(application.scanBasePackages());

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
                    pluginLifeCycleMap.put(OnLoad.class, method);
                }
                if (method.isAnnotationPresent(OnEnable.class)) {
                    pluginLifeCycleMap.put(OnEnable.class, method);
                }
                if (method.isAnnotationPresent(OnDisable.class)) {
                    pluginLifeCycleMap.put(OnDisable.class, method);
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
        Method method;
        if ((method = pluginLifeCycleMap.get(OnLoad.class)) != null) {
            try {
                method.invoke(delegate);
            } catch (Exception e) {
                throw new PluginException(e);
            }
        }
    }

    @Override
    public void onEnable() {
        Method method;
        if ((method = pluginLifeCycleMap.get(OnEnable.class)) != null) {
            try {
                method.invoke(delegate);
            } catch (Exception e) {
                throw new PluginException(e);
            }
        }
    }

    @Override
    public void onDisable() {
        Method method;
        if ((method = pluginLifeCycleMap.get(OnDisable.class)) != null) {
            try {
                method.invoke(delegate);
            } catch (Exception e) {
                throw new PluginException(e);
            }
        }
    }

}
