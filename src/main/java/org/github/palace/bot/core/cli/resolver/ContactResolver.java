package org.github.palace.bot.core.cli.resolver;

import lombok.EqualsAndHashCode;
import net.mamoe.mirai.contact.Contact;
import org.github.palace.bot.core.cli.CommandSender;

/**
 * @author jihongyuan
 * @date 2022/5/9 16:51
 */

@EqualsAndHashCode
public class ContactResolver implements Resolver{

    @Override
    public boolean supportParameter(Class<?> parameter) {
        return Contact.class == parameter;
    }

    @Override
    public <T> Object resolveArgument(T obj) {
        return obj instanceof CommandSender ? ((CommandSender) obj).getSubject() : null;
    }

}
