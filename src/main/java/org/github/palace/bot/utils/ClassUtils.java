package org.github.palace.bot.utils;

/**
 * @author jihongyuan
 * @date 2022/6/22 14:55
 */
public final class ClassUtils {
    private ClassUtils() {
    }

    public static String resolveDescriptor(String descriptor) {
        char tag = descriptor.charAt(0);

        switch (tag) {
            case 'B':
                return "byte";
            case 'C':
                return "char";
            case 'D':
                return "double";
            case 'F':
                return "float";
            case 'I':
                return "int";
            case 'J':
                return "long";
            case 'L':
                return descriptor.substring(1, descriptor.length() - 1).replace("/", ".");
            case 'S':
                return "short";
            case 'Z':
                return "boolean";
            case '[':
                // TODO
            default:
                throw new IllegalArgumentException("Invalid descriptor: " + tag);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T forName(String className) {
        try {
            return (T) Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException(e);
        }
    }

}
