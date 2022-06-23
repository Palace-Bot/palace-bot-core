package org.github.palace.bot.asm;

import lombok.Getter;

/**
 * @author jihongyuan
 * @date 2022/6/8 9:19
 */
@Getter
public abstract class ClassVisitor {

    public void visit(int access, String className, String superName, String[] interfaces) {
        return;
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

    public abstract void visitEnd();

    public void visitSourceFile(String sourcefile) {
    }

}
