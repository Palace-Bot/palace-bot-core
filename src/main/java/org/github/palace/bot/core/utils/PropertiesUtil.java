package org.github.palace.bot.core.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author jihongyuan
 * @date 2022/5/14 21:25
 */
public final class PropertiesUtil {

    /**
     * Save class mapping field data
     */
    private static final Map<Class<?>, Field[]> PROPERTIES_MAP = new HashMap<>(16);

    private PropertiesUtil() {
    }

    /**
     * 获取 映射类 熟悉键
     *
     * @param clazz properties mapping class
     */
    public static synchronized Field[] getPropertiesKeys(Class<?> clazz) {
        if (PROPERTIES_MAP.containsKey(clazz)) {
            return PROPERTIES_MAP.get(clazz);
        }

        List<Field> fieldList = new ArrayList<>();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (
                    Modifier.isPublic(field.getModifiers()) &&
                    !Modifier.isFinal(field.getModifiers()) &&
                    !Modifier.isStatic(field.getModifiers())) {
                fieldList.add(field);
            }
        }

        Field[] result = fieldList.toArray(new Field[0]);
        PROPERTIES_MAP.put(clazz, result);
        return result;
    }
}
