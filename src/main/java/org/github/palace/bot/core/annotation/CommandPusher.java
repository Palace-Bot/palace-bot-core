package org.github.palace.bot.core.annotation;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * 命令主动推送
 *
 * @author jihongyuan
 * @date 2022/4/7 16:39
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CommandPusher {

    /**
     * 时间类型
     */
    TimeUnit unit() default TimeUnit.SECONDS;

    /**
     * 时间
     */
    long value() default 60;

}