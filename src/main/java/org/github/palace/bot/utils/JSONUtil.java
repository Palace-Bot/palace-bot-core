package org.github.palace.bot.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
/**
 * @author JHY
 * @date 2022/3/28 11:08
 */
public final class JSONUtil {

    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private JSONUtil() {
    }

    public static <T> T readValue(InputStream is, Class<T> clazz) {
        try {
            return OBJECT_MAPPER.readValue(is, clazz);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T readValue(String str, Class<T> clazz) {
        try {
            return OBJECT_MAPPER.readValue(str, clazz);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String toJsonString(Object obj) {
        try {
            return OBJECT_MAPPER.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
