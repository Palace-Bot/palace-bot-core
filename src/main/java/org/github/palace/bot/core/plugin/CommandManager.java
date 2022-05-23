package org.github.palace.bot.core.plugin;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.Bot;
import org.github.palace.bot.core.LifeCycle;
import org.github.palace.bot.core.annotation.CommandPusher;
import org.github.palace.bot.core.cli.support.InvocableMethod;
import org.github.palace.bot.core.cli.support.ParameterResolver;

import javax.annotation.Nullable;
import java.lang.reflect.Method;
import java.util.*;

/**
 * 全局命令管理器，用于执行命令
 *
 * @author JHY
 * @date 2022/3/27 15:28
 */
@Slf4j
public class CommandManager implements LifeCycle {

    /**
     * 命令前缀
     */
    @Getter
    private final String commandPrefix;

    private final PluginWrapper plugin;

    @Getter
    private final Set<AbstractCommand> commands = new HashSet<>();

    public CommandManager(String commandPrefix, PluginWrapper plugin) {
        this.commandPrefix = commandPrefix;
        this.plugin = plugin;
    }

    @Nullable
    public AbstractCommand matchCommand(String commandName) {
        for (AbstractCommand command : commands) {
            if (commandName.equals(commandPrefix + command.getPrimaryName())) {
                return command;
            }
        }
        return null;
    }


    public void addCommand(AbstractCommand command) {
        commands.add(command);
    }

    @Override
    public void start() {
        for (AbstractCommand command : commands) {
            log.debug("start command: {}", command.getPrimaryName());
            Map<Method, CommandPusher> commandPushMethodMap = command.getCommandPusherMethodMap();
            commandPushMethodMap.forEach((method, commandPusher) -> plugin.getPusherExecutorService().scheduleAtFixedRate(() -> {

                ParameterResolver resolver = new ParameterResolver(Loc.get(Bot.class));
                Object[] args = resolver.resolver(method.getParameterTypes());
                InvocableMethod invocableMethod = new InvocableMethod(command, method, args);
                try {
                    invocableMethod.doInvoke();
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            }, 0, commandPusher.value(), commandPusher.unit()));
        }
    }

    @Override
    public void stop() {
    }

}
