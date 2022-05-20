package org.github.palace.bot.core.cli.resolver;

import lombok.EqualsAndHashCode;
import net.mamoe.mirai.Bot;
import org.github.palace.bot.core.cli.CommandSender;

/**
 * @author jihongyuan
 * @date 2022/5/9 16:51
 */

@EqualsAndHashCode
public class BotResolver implements Resolver {

    @Override
    public boolean supportParameter(Class<?> parameter) {
        return Bot.class == parameter;
    }

    @Override
    public <T> Object resolveArgument(T obj) {
        if (obj instanceof CommandSender) {
            return ((CommandSender) obj).getBot();
        } else if (obj instanceof Bot) {
            return obj;
        }
        return null;
    }


}
