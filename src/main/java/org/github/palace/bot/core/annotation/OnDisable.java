package org.github.palace.bot.core.annotation;

import java.lang.annotation.*;

/**
 * @author jihongyuan
 * @date 2022/6/1 16:46
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OnDisable {
}
