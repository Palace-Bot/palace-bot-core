package org.github.palace.bot.core.cli;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author JHY
 * @date 2022/3/27 9:45
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Command {

    /**
     * 主指令名. 将会参与构成 [Permission.id].
     * <p>
     * 不允许包含 [空格][Char.isWhitespace], '.', ':'.
     */
    private String primaryName;

    // TODO 还没想好
    /**
     * 为此指令分配的权限.
     */
    private Void permission;

    /**
     * 为 true 时, 需要 Y/n 确定
     */
    private boolean determine;

    /**
     * 描述, 用于显示在 [BuiltInCommands.HelpCommand]
     */
    private String description;

}
