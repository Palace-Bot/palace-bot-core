package org.github.palace.bot.asm;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jihongyuan
 * @date 2022/6/23 10:39
 */
public class SimpleMetadata {

    private final int access;

    private final String className;

    private final String superName;

    private final String[] interfaces;

    private final List<MergedAnnotation<?>> annotations;

    public SimpleMetadata(int access, String className, String superName, String[] interfaces, List<MergedAnnotation<?>> annotations) {
        this.access = access;
        this.className = className;
        this.superName = superName;
        this.interfaces = interfaces;
        this.annotations = annotations;
    }

    public boolean hasAnnotation(Class<?> clazz) {
        for (MergedAnnotation<?> annotation : annotations) {
            if (annotation.getType().equals(clazz)) {
                return true;
            }
        }
        return false;
    }

}
