package org.github.palace.bot.core.plugin;


import lombok.extern.slf4j.Slf4j;
import org.github.palace.bot.core.annotation.Application;
import org.github.palace.bot.core.annotation.OnDisable;
import org.github.palace.bot.core.annotation.OnEnable;
import org.github.palace.bot.core.annotation.OnLoad;
import org.github.palace.bot.core.exception.PluginException;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.Collections;
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

    private final Map<Class<?>, Method> pluginLifeCycleMap = new HashMap<>(16);

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

        parseLifeCycle();
    }

    protected void parseLifeCycle() {
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

    protected void invokeLifeCycle(Class<?> lifeCycleClass) {
        Method method = pluginLifeCycleMap.get(lifeCycleClass);

        if (method == null) {
            method = ReflectionUtils.findMethod(delegateClass, lifeCycleClass.getSimpleName());
        }

        if (method != null) {
            try {
                ReflectionUtils.invokeMethod(method, delegate);
            } catch (Exception e) {
                throw new PluginException(e);
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
        invokeLifeCycle(OnLoad.class);
    }

    @Override
    public void onEnable() {
        invokeLifeCycle(OnEnable.class);
    }

    @Override
    public void onDisable() {
        invokeLifeCycle(OnDisable.class);
    }


    //  调用被代理类方法

    @Override
    public List<AbstractCommand> getCommands() {
        Method method = ReflectionUtils.findMethod(delegateClass, "getCommands");
        if (method == null) {
            return Collections.emptyList();
        }
        return (List<AbstractCommand>) ReflectionUtils.invokeMethod(method, delegate);
    }

    @Override
    protected Plugin register(AbstractCommand command) {
        Method method = ReflectionUtils.findMethod(delegateClass, "register", AbstractCommand.class);
        if (method != null) {
            ReflectionUtils.invokeMethod(method, delegate, command);
        }
        return this;
    }

}
