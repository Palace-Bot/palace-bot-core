package org.github.palace.bot.asm;

/**
 * @author jihongyuan
 * @date 2022/6/9 10:02
 */
public abstract class AnnotationVisitor {
    public void visitEnd() {
    }

    public void visit(final String name, final Object value) {
    }

    public AnnotationVisitor visitArray(String elementName) {
        return null;
    }

    public AnnotationVisitor visitAnnotation(String elementName, String descriptor) {
        return null;
    }

    public void visitEnum(String elementName, String descriptor, String value) {
    }

}
