package org.github.palace.bot.asm;

import lombok.Getter;
import org.github.palace.bot.utils.JSONUtil;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.util.Map;

/**
 * @author jihongyuan
 * @date 2022/6/21 11:05
 */
public class MergedAnnotation<A extends Annotation> implements Serializable {

    @Getter
    private final Map<String, Object> attributes;

    @Getter
    private final Class<A> type;

    public MergedAnnotation(Class<A> type, Map<String, Object> attributes) {
        this.type = type;
        this.attributes = attributes;
    }

    @Override
    public String toString() {
        return JSONUtil.toJsonString(this);
    }

}
