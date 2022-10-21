package org.github.palace.bot.core.annotation;

import java.lang.annotation.*;

/**
 * @author jihongyuan
 * @date 2022/5/7 15:47
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Application {

    String version() default "";

    String name() default "";

    String description() default "";

    String[] scanBasePackages() default {};

}
