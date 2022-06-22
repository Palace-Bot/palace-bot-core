package org.github.palace.bot.asm;

/**
 * @author jihongyuan
 * @date 2022/6/9 10:02
 */
public abstract class AnnotationVisitor {
    public void visitEnd() {
    }

    public void visit(final String name, final Object value) {
        System.out.println("visit: " + name + " " + value);
    }

    public AnnotationVisitor visitArray(String elementName) {
        System.out.println("visitArray: " + elementName);
        return this;
    }

    public AnnotationVisitor visitAnnotation(String elementName, String descriptor) {
        System.out.println("visitAnnotation: " + elementName + " " + descriptor);
        return this;
    }

    public void visitEnum(String elementName, String descriptor, String value) {
        System.out.println("visitEnum: " + elementName + " " + descriptor + " " + value);
    }

}
