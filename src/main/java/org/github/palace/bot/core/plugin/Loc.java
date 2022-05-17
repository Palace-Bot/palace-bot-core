package org.github.palace.bot.core.plugin;

import java.util.HashMap;
import java.util.Map;

/**
 * @author jihongyuan
 * @date 2022/5/17 16:01
 */
public class Loc {
    static Map<Class<?>, Object> loc_mapping = new HashMap<>();

    public static <T> T get(Class<T> clazz) {
        return (T) loc_mapping.get(clazz);
    }

    public static <T> void put(Class<T> clazz, T t) {
       loc_mapping.put(clazz, t);
    }

}
