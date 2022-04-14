package org.github.palace.bot.core.cli.support;

import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.contact.MemberPermission;
import net.mamoe.mirai.contact.User;
import net.mamoe.mirai.message.data.*;
import org.github.palace.bot.core.cli.AbstractCommand;
import org.github.palace.bot.core.cli.CommandSender;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author JHY
 * @date 2022/4/2 16:58
 */
public class ParameterMapping {

    // TODO 可以优化，暂时先写死
    private static final Map<Class<?>, Integer> CLASS_TABLE = new HashMap<>();

    static {
        // 基本
        CLASS_TABLE.put(CommandSender.class, 0);
        CLASS_TABLE.put(Bot.class, 1);
        CLASS_TABLE.put(Contact.class, 2);
        CLASS_TABLE.put(User.class, 3);
        CLASS_TABLE.put(String.class, 4);

        // message
        CLASS_TABLE.put(PlainText.class, 5);
        CLASS_TABLE.put(At.class, 6);
        CLASS_TABLE.put(AtAll.class, 7);
        CLASS_TABLE.put(HummerMessage.class, 8);
        CLASS_TABLE.put(Image.class, 9);
        CLASS_TABLE.put(RichMessage.class, 10);
        CLASS_TABLE.put(ServiceMessage.class, 11);
        CLASS_TABLE.put(Face.class, 12);
        CLASS_TABLE.put(ForwardMessage.class, 13);
        CLASS_TABLE.put(Audio.class, 14);
        CLASS_TABLE.put(MarketFace.class, 15);
        CLASS_TABLE.put(MusicShare.class, 16);
    }

    private ParameterMapping() {
    }

    @Nullable
    public static InvocableMethod mapping(MemberPermission permission, AbstractCommand command, Object[] args) {
        Set<Method> methods = command.getCommandHandlerMethodMap().keySet();
        for (Method method : methods) {
            Class<?>[] parameterTypes = method.getParameterTypes();
            Object[] params = new Object[parameterTypes.length];
            int current = 0;
            for (Class<?> parameterType : parameterTypes) {
                for (Object arg : args) {
                    if (arg != null && parameterType.isAssignableFrom(arg.getClass()) && permission.getLevel() >= command.getPermission().getLevel()) {
                        params[current++] = arg;
                    }
                }
            }
            if (current == parameterTypes.length) {
                return new InvocableMethod(command, method, params);
            }

        }
        return null;
    }

}
