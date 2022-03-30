package org.github.palace.bot.core.io;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 抄自Spring源码
 *
 * @author JHY
 * @date 2022/3/23 9:09
 */
public final class BotFactoriesLoader {
    /**
     * The location to look for factories.
     * <p>Can be present in multiple JAR files.
     */
    public static final String FACTORIES_RESOURCE_LOCATION = "META-INF/bot.factories";

    private static final Map<ClassLoader, Map<String, List<String>>> CACHE = new ConcurrentHashMap<>();

    private BotFactoriesLoader() {
    }

    public static List<String> loadFactoryNames(Class<?> factoryClass, ClassLoader classLoader) {
        String factoryClassName = factoryClass.getName();
        return loadSpringFactories(classLoader).getOrDefault(factoryClassName, Collections.emptyList());
    }

    private static Map<String, List<String>> loadSpringFactories(ClassLoader classLoader) {
        Map<String, List<String>> result = CACHE.get(classLoader);
        if (result != null) {
            return result;
        }

        try {
            Enumeration<URL> urls = (classLoader != null ?
                    classLoader.getResources(FACTORIES_RESOURCE_LOCATION) :
                    ClassLoader.getSystemResources(FACTORIES_RESOURCE_LOCATION));
            result = new HashMap<>(16);
            while (urls.hasMoreElements()) {
                URL url = urls.nextElement();

                Properties properties = new Properties();

                URLConnection con = url.openConnection();
                try (InputStream is = con.getInputStream()) {
                    properties.load(is);
                }

                for (Map.Entry<?, ?> entry : properties.entrySet()) {
                    String factoryClassName = ((String) entry.getKey()).trim();
                    List<String> factoryNames = new ArrayList<>(Arrays.asList(((String) entry.getValue()).split(" ")));
                    result.put(factoryClassName, factoryNames);
                }
            }
            CACHE.put(classLoader, result);
            return result;
        } catch (IOException ex) {
            throw new IllegalArgumentException("Unable to load factories from location [" +
                    FACTORIES_RESOURCE_LOCATION + "]", ex);
        }
    }

}
