package org.github.palace.bot.core.constant;

import org.github.palace.bot.core.io.YamlLoader;

import static org.github.palace.bot.utils.TypeUtil.cast;

/**
 * @author jihongyuan
 * @date 2022/4/11 15:55
 */
public class BaseConstant {
    public static final Long USER;

    static {
        USER = cast(YamlLoader.loadYamlNames("bot.base.user", BaseConstant.class.getClassLoader()), Long.class);
    }

}
