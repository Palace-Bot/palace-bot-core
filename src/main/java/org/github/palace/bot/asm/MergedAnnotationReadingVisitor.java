package org.github.palace.bot.asm;

import lombok.Getter;
import org.github.palace.bot.utils.ClassUtils;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * @author jihongyuan
 * @date 2022/6/21 10:52
 */
@Getter
public class MergedAnnotationReadingVisitor<A extends Annotation> extends AnnotationVisitor {

    private final Map<String, Object> attributes = new LinkedHashMap<>(4);

    private final String descriptor;

    private final Consumer<MergedAnnotation<A>> consumer;

    private MergedAnnotationReadingVisitor(String descriptor, Consumer<MergedAnnotation<A>> consumer) {
        this.descriptor = descriptor;
        this.consumer = consumer;
    }

    @Override
    public void visit(String name, Object value) {
        attributes.put(name, value);
    }

    @Override
    public void visitEnum(String elementName, String descriptor, String value) {
        String className = ClassUtils.resolveDescriptor(descriptor);
        attributes.put(elementName, Enum.valueOf(ClassUtils.forName(className), value));
    }

    @Override
    public AnnotationVisitor visitAnnotation(String elementName, String descriptor) {
        return new MergedAnnotationReadingVisitor<>(elementName, consumer);
    }

    @Override
    public AnnotationVisitor visitArray(String elementName) {
        return new ArrayVisitor(value -> attributes.put(elementName, value));
    }

    @Override
    public void visitEnd() {
        consumer.accept(new TypeMappedAnnotation<>(attributes));
    }

    static <A extends Annotation> AnnotationVisitor get(String descriptor, Consumer<MergedAnnotation<A>> consumer) {
        return new MergedAnnotationReadingVisitor<>(descriptor, consumer);
    }

    /**
     * {@link AnnotationVisitor} to deal with array attributes.
     */
    private class ArrayVisitor extends AnnotationVisitor {

        private final List<Object> elements = new ArrayList<>();

        private final Consumer<Object[]> consumer;

        private ArrayVisitor(Consumer<Object[]> consumer) {
            this.consumer = consumer;
        }

        @Override
        public void visitEnum(String elementName, String descriptor, String value) {
            String className = ClassUtils.resolveDescriptor(descriptor);
            elements.add(Enum.valueOf(ClassUtils.forName(className), value));
        }

        @Override
        public AnnotationVisitor visitAnnotation(String elementName, String descriptor) {
            return new MergedAnnotationReadingVisitor<>(elementName, elements::add);
        }

        @Override
        public void visit(String name, Object value) {
            elements.add(value);
        }

        @Override
        public void visitEnd() {
            consumer.accept(elements.toArray());
        }

    }
}
