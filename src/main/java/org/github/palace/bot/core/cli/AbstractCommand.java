package org.github.palace.bot.core.cli;

import lombok.Getter;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author JHY
 * @date 2022/3/31 15:44
 */
public abstract class AbstractCommand extends Command{

    @Getter
    private final Map<Method, Class<?>[]> handlerParameterTypesMap = new HashMap<>();

    protected AbstractCommand(String primaryName, Void permission, boolean determine, String description) {
        super(primaryName, permission, determine, description);
    }

    public void putParameterTypes(Method method, Class<?>[] parameterTypes) {
        handlerParameterTypesMap.put(method, parameterTypes);
    }

}