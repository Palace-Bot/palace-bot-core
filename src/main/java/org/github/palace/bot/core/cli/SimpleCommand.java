package org.github.palace.bot.core.cli;

import net.mamoe.mirai.contact.MemberPermission;

/**
 * 目前无作用
 *
 * @author JHY
 * @date 2022/3/30 16:30
 */
public class SimpleCommand extends AbstractCommand {
    public SimpleCommand(String primaryName, MemberPermission permission, boolean determine, String description) {
        super(primaryName, permission, determine, description);
    }
}
