package org.github.palace.bot.utils;

/**
 * @author JHY
 * @date 2022/3/23 23:03
 */
public final class TypeUtil {

    private TypeUtil() {
    }

    @SuppressWarnings("unchecked")
    public static <T> T cast(Object obj, Class<T> clazz) {
        if (clazz.equals(Long.class)) {
            obj = Long.valueOf(obj.toString());
        }
        return (T) obj;
    }

}
