package org.github.palace.bot.asm;

import lombok.Getter;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

/**
 * @author jihongyuan
 * @date 2022/6/21 10:02
 */
public class AnnotationMetadataReadingVisitor extends ClassVisitor {
    private int access;

    private String className;

    private String superName;

    private String[] interfaces;

    @Getter
    private List<MergedAnnotation<?>> annotations = new ArrayList<>();

    @Override
    public void visit(int access, String className, String superName, String[] interfaces) {
        super.visit(access, className, superName, interfaces);
        this.access = access;
        this.className = className;
        this.superName = superName;
        this.interfaces = interfaces;
    }

    @Override
    public AnnotationVisitor visitVisibleAnnotation(String descriptor) {
        return MergedAnnotationReadingVisitor.get(descriptor, annotations::add);
    }

    @Override
    public void visitEnd() {
        super.visitEnd();
    }
}
