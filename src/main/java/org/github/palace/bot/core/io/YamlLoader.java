package org.github.palace.bot.core.io;

import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author JHY
 * @date 2022/3/23 22:24
 */

public final class YamlLoader {

    /**
     * The location to look for factories.
     * <p>Can be present in multiple JAR files.
     */
    public static final String FACTORIES_RESOURCE_LOCATION = "application.yml";

    private static final Map<ClassLoader, Map<String, Object>> CACHE = new ConcurrentHashMap<>();

    private YamlLoader() {
    }

    public static Object loadYamlNames(String key, ClassLoader classLoader) {
        Map<String, Object> propertyMap = CACHE.get(classLoader);
        if (propertyMap != null) {
            return propertyMap.get(key);
        }

        propertyMap = new HashMap<>(16);
        CACHE.put(classLoader, propertyMap);
        try {
            Enumeration<URL> urls = (classLoader != null ?
                    classLoader.getResources(FACTORIES_RESOURCE_LOCATION) :
                    ClassLoader.getSystemResources(FACTORIES_RESOURCE_LOCATION));
            while (urls.hasMoreElements()) {
                URL url = urls.nextElement();

                URLConnection con = url.openConnection();
                try (InputStream is = con.getInputStream()) {
                    Yaml yaml = new Yaml();
                    for (Object data : yaml.loadAll(is)) {
                        parse(classLoader, data, new ArrayList<>());
                    }
                }
            }
        } catch (IOException ex) {
            throw new IllegalArgumentException("Unable to load factories from location [" +
                    FACTORIES_RESOURCE_LOCATION + "]", ex);
        }

        return propertyMap.get(key);
    }

    /**
     * 回溯yaml, 添加到缓存中
     *
     * @param classLoader application
     * @param data        数据域
     * @param keys        回溯前缀key
     */
    @SuppressWarnings("unchecked")
    private static void parse(ClassLoader classLoader, Object data, List<String> keys) {
        if (data instanceof Map) {
            ((Map<String, Object>) data).forEach((k, v) -> {
                keys.add(k);
                parse(classLoader, v, keys);
            });
        } else {
            String key = String.join(".", keys);
            CACHE.get(classLoader).put(key, data);
            keys.remove(keys.size() - 1);
        }
    }

}
