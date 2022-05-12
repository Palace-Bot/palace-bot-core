package org.github.palace.bot.core.cli.resolver;

import lombok.EqualsAndHashCode;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.User;
import org.github.palace.bot.core.cli.CommandSender;

/**
 * @author jihongyuan
 * @date 2022/5/9 16:52
 */

@EqualsAndHashCode
public class UserResolver implements Resolver {
    @Override
    public boolean supportParameter(Class<?> parameter) {
        return User.class == parameter;
    }

    @Override
    public Object resolveArgument(CommandSender commandSender) {
        return commandSender.getUser();
    }

}
