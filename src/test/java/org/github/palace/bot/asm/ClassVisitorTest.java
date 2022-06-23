package org.github.palace.bot.asm;

import org.junit.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

/**
 * @author jihongyuan
 * @date 2022/6/21 16:44
 */
public class ClassVisitorTest {

    @Test
    public void test() {
        try (InputStream is = new FileInputStream("F:\\idea_workspace\\Palace-Bot\\palace-bot-core\\target\\test-classes\\org\\test\\asm\\AsmClassTest.class")) {
            ClassReader classReader = new ClassReader(ClassReader.readStream(is));
            SimpleMetadataReadingVisitor visitor = new SimpleMetadataReadingVisitor();
            classReader.accept(visitor);

            List<MergedAnnotation<?>> annotations = visitor.getAnnotations();

            System.out.println("access: " + visitor.getAccess());
            System.out.println("className: " + visitor.getClassName());
            System.out.println("superName: " + visitor.getSuperName());
            System.out.println("interfaces" + Arrays.toString(visitor.getInterfaces()));

            for (MergedAnnotation<?> annotation : annotations) {
                System.out.println(annotation);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
