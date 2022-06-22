package org.github.palace.bot.asm;

import lombok.Getter;

/**
 * @author jihongyuan
 * @date 2022/6/8 9:19
 */
@Getter
public abstract class ClassVisitor {

    protected int access;
    protected String name;
    protected String superName;
    protected String[] interfaces;

    public void visit(int access, String className, String superName, String[] interfaces) {
        this.access = access;
        this.name = className;
        this.superName = superName;
        this.interfaces = interfaces;
    }

    public void visitField(FieldInfo fieldInfo) {
    }

    public void visitMethod(MethodInfo methodInfo) {
    }

    public AnnotationVisitor visitVisibleAnnotation(String descriptor) {
        return null;
    }

    public AnnotationVisitor visitInvisibleAnnotation(String descriptor) {
        return null;
    }

    public AnnotationVisitor visitVisibleParameterAnnotation(String descriptor) {
        return null;
    }

    public AnnotationVisitor visitInvisibleParameterAnnotation(String descriptor) {
        return null;
    }

    public void visitEnd() {
    }

}
