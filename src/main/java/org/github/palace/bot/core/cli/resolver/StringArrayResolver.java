package org.github.palace.bot.core.cli.resolver;

import lombok.EqualsAndHashCode;
import net.mamoe.mirai.message.data.PlainText;
import net.mamoe.mirai.message.data.SingleMessage;
import org.apache.commons.lang3.StringUtils;
import org.github.palace.bot.core.cli.CommandSender;
import org.github.palace.bot.core.plugin.PluginProperties;

import java.util.StringJoiner;

/**
 * 解析字符串数组
 *
 * @author jihongyuan
 * @date 2022/11/3 11:08
 */
@EqualsAndHashCode
public class StringArrayResolver implements Resolver {
    @Override
    public boolean supportParameter(Class<?> parameter) {
        return String[].class == parameter;
    }

    @Override
    public <T> Object resolveArgument(T obj) {
        if (obj instanceof CommandSender) {
            CommandSender commandSender = (CommandSender) obj;
            StringJoiner sj = new StringJoiner(" ");
            for (SingleMessage singleMessage : commandSender.getMessageChain()) {
                if (singleMessage instanceof PlainText) {
                    String command = singleMessage.toString();
                    for (String s : command.split(" ")) {
                        if (StringUtils.isNotBlank(s) && !s.startsWith(PluginProperties.commandPrefix)) {
                            sj.add(s);
                        }
                    }
                }
            }

            String str = sj.toString();
            return str.split(" ");
        }
        return null;
    }

}
