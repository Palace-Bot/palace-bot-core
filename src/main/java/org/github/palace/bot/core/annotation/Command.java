package org.github.palace.bot.core.annotation;

import net.mamoe.mirai.contact.MemberPermission;
import org.github.palace.bot.core.CommandScope;
import org.github.palace.bot.core.cli.CommandSession;

import java.lang.annotation.*;

/**
 * @author jihongyuan
 * @date 2022/6/1 17:25
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Command {

    String primaryName() default "";

    MemberPermission permission() default MemberPermission.MEMBER;

    CommandScope scope() default CommandScope.GROUP_MEMBER;

    boolean determine() default false;

    String description() default "";

    Class<?>[] childCommands() default {};

    CommandSession.State state() default CommandSession.State.RUNNABLE;

}
