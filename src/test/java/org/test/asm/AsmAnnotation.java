package org.test.asm;

import java.lang.annotation.*;

/**
 * @author jihongyuan
 * @date 2022/6/21 17:27
 */
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AsmAnnotation {

    byte byteValue() default 0;

    char charValue() default '\0';

    double doubleValue() default 0.0d;

    float floatValue() default 0.0f;

    int intValue() default 0;

    long longValue() default 0L;

    short shortValue() default 0;

    boolean booleanValue() default false;

    String stringValue() default "";

    IEnum enumValue();

    Class<?> classValue() default Object.class;

    Mapping[] arrayValue() default {};

    Mapping mapping();

}

@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@interface Mapping {
    String str() default "";

}

enum IEnum {
    TEST
}
