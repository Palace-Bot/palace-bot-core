package org.github.palace.bot.asm;

import lombok.Getter;
import org.github.palace.bot.utils.ClassUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jihongyuan
 * @date 2022/6/21 10:02
 */
@Getter
public class SimpleMetadataReadingVisitor extends ClassVisitor {

    private int access;

    private String className;

    private String superName;

    private String[] interfaces;

    @Getter
    private SimpleMetadata metadata;

    private final List<MergedAnnotation<?>> annotations = new ArrayList<>();

    @Override
    public void visit(int access, String className, String superName, String[] interfaces) {
        this.access = access;
        this.className = className;
        this.superName = superName;
        this.interfaces = interfaces;
    }

    @Override
    public AnnotationVisitor visitVisibleAnnotation(String descriptor) {
        String className = ClassUtils.resolveDescriptor(descriptor);
        return MergedAnnotationReadingVisitor.get(ClassUtils.forName(className), annotations::add);
    }

    // TODO more attributes annotation

    @Override
    public void visitEnd() {
        metadata = new SimpleMetadata(access, className, superName, interfaces, annotations);
    }

}
