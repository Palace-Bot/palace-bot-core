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
    public Object resolveArgument(CommandSender commandSender) {
        return commandSender.getBot();
    }

    @Override
    public Bot resolveArgument(Bot bot) {
        return bot;
    }

}
