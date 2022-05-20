package org.github.palace.bot.core.cli.resolver;


/**
 * @author jihongyuan
 * @date 2022/5/9 16:28
 */
public interface Resolver {

    /**
     * Is support parameter
     *
     * @return true / false
     */
    boolean supportParameter(Class<?> parameter);

    /**
     * Resolver argument
     *
     * @param objs objs
     * @return resolver after data
     */
    default Object resolveArgument(Object... objs) {
        Object result = null;
        for (Object obj : objs) {
            result = resolveArgument(obj);
            if (result != null) {
                break;
            }
        }
        return result;
    }

    <T> Object resolveArgument(T obj);

}
