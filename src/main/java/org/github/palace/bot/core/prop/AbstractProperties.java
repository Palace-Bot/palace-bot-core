package org.github.palace.bot.core.prop;

import lombok.extern.slf4j.Slf4j;
import org.github.palace.bot.core.utils.PropertiesUtil;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Properties;

/**
 * @author jihongyuan
 * @date 2022/5/14 21:56
 */
@Slf4j
public abstract class AbstractProperties {

    public AbstractProperties(String path) {
        this(path, Thread.currentThread().getContextClassLoader());
    }

    /**
     * TODO key 可以设置必填和非必填
     *
     * @param path properties file path
     */
    public AbstractProperties(String path, ClassLoader classLoader) {
        try (InputStream inputStream = classLoader.getResourceAsStream(path)) {
            Properties properties = new Properties();
            properties.load(inputStream);

            Field[] propertiesKeys = PropertiesUtil.getPropertiesKeys(this.getClass());
            for (Field field : propertiesKeys) {
                field.set(this, properties.getProperty(getPropertiesPrefix() + "." + field.getName()));
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 获取属性前缀名
     */
    public abstract String getPropertiesPrefix();

}
