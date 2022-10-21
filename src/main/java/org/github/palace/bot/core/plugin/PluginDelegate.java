package org.github.palace.bot.core.plugin;


import lombok.extern.slf4j.Slf4j;
import org.github.palace.bot.core.annotation.Application;
import org.github.palace.bot.core.annotation.OnDisable;
import org.github.palace.bot.core.annotation.OnEnable;
import org.github.palace.bot.core.annotation.OnLoad;
import org.github.palace.bot.core.exception.PluginException;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author jihongyuan
 * @date 2022/6/1 16:40
 */
@Slf4j
public class PluginDelegate extends Plugin {

    private final Class<?> delegateClass;

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
        } else {
            try {
                ReflectionUtils.findMethod(delegateClass, "onLoad").invoke(delegate);
            } catch (Exception e) {
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

    @Override
    public List<AbstractCommand> getCommands() {
        try {
            return (List<AbstractCommand>) ReflectionUtils.findMethod(delegateClass, "getCommands").invoke(delegate);
        } catch (Exception e) {
        }
        return null;
    }

    @Override
    protected Plugin register(AbstractCommand command) {
        try {
            ReflectionUtils.findMethod(delegateClass, "register", AbstractCommand.class).invoke(delegate, command);
        } catch (Exception e) {
        }
        return this;
    }
}
