package org.github.palace.bot.core.plugin;

import net.mamoe.mirai.contact.MemberPermission;
import org.github.palace.bot.core.CommandScope;

/**
 * 目前无作用
 *
 * @author JHY
 * @date 2022/3/30 16:30
 */
public class SimpleCommand extends AbstractCommand {

    public SimpleCommand(String primaryName, MemberPermission permission, String description) {
        super(primaryName, permission, description);
    }

    public SimpleCommand(String primaryName, String description) {
        super(primaryName, description);
    }

    public SimpleCommand(String primaryName, MemberPermission permission, boolean determine, String description) {
        super(primaryName, permission, determine, description);
    }

    protected SimpleCommand(String primaryName, MemberPermission permission, CommandScope commandScope, boolean determine, String description) {
        super(primaryName, permission, commandScope, determine, description);
    }

}
