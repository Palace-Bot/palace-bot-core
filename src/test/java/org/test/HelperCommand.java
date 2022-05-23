package org.test;

import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.MemberPermission;
import org.github.palace.bot.core.annotation.ChildCommandHandler;
import org.github.palace.bot.core.annotation.CommandHandler;
import org.github.palace.bot.core.annotation.CommandPusher;
import org.github.palace.bot.core.plugin.AbstractCommand;
import org.github.palace.bot.core.cli.CommandSender;

/**
 * @author jihongyuan
 * @date 2022/5/10 10:08
 */
public class HelperCommand extends AbstractCommand {

    public HelperCommand() {
        super("帮助", MemberPermission.MEMBER, false, "--help");
    }

    @CommandHandler
    public void handler(CommandSender commandSender) {
    }

    @CommandPusher
    public void pusher(Bot bot) {
    }

    @ChildCommandHandler(primaryName = "-- help")
    public void childHelpHandler(CommandSender commandSender) {
    }

}
