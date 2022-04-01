package org.github.palace.bot.core.annotation;

import java.lang.annotation.*;

/**
 * @author JHY
 * @date 2022/3/31 9:54
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CommandHandler {
}