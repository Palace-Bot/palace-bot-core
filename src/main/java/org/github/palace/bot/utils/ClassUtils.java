package org.github.palace.bot.utils;

/**
 * @author jihongyuan
 * @date 2022/6/22 14:55
 */
public final class ClassUtils {
    private ClassUtils() {
    }

    public static String resolveDescriptor(String descriptor) {
        return descriptor.substring(1, descriptor.length() - 1).replaceAll("\\/", "\\.");
    }

    @SuppressWarnings("unchecked")
    public static <T> T forName(String className) {
        try {
            return (T) Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
