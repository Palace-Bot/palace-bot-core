package org.github.palace.bot.asm;

import lombok.Getter;
import org.github.palace.bot.utils.JSONUtil;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.util.Map;

/**
 * @author jihongyuan
 * @date 2022/6/21 11:12
 */
public class TypeMappedAnnotation<A extends Annotation> implements MergedAnnotation<A>, Serializable {

    @Getter
    private final Map<String, Object> attributes;

    public TypeMappedAnnotation(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String toString() {
        return JSONUtil.toJsonString(attributes);
    }
}
