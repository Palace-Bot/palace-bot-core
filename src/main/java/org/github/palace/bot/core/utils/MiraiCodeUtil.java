package org.github.palace.bot.core.utils;

/**
 * @author JHY
 * @date 2022/3/23 23:48
 */
public final class MiraiCodeUtil {
    private static final String AT_ME_MIRAI_CODE = "mirai:at:";

    private MiraiCodeUtil() {
    }

    public static boolean isAtMe(String mraiCode, Long botQQ) {
        return mraiCode.contains("[" + AT_ME_MIRAI_CODE + botQQ + "]");
    }

    public static boolean isCommandLine(String mraiCode) {
        return true;
    }

}
