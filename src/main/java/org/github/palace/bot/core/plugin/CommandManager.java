package org.github.palace.bot.core.plugin;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.MemberPermission;
import org.github.palace.bot.core.annotation.CommandPusher;
import org.github.palace.bot.core.cli.AbstractCommand;
import org.github.palace.bot.core.cli.CommandSender;
import org.github.palace.bot.core.cli.CommandSession;
import org.github.palace.bot.core.cli.support.InvocableMethod;
import org.github.palace.bot.core.cli.support.ParameterResolver;

import javax.annotation.Nullable;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * 全局命令管理器，用于执行命令
 *
 * @author JHY
 * @date 2022/3/27 15:28
 */
@Slf4j
public class CommandManager {

    /**
     * 命令前缀
     */
    @Getter
    private final String commandPrefix;

    /**
     * 机器人信息
     */
    private final Bot bot;

    public CommandManager(String commandPrefix, Bot bot) {
        this.commandPrefix = commandPrefix;
        this.bot = bot;
    }

    public void executeCommand(CommandSender commandSender, AbstractCommand command, MemberPermission permission, CommandSession session) {
        // (1) Get command method parameterTypes
        Map<Method, Class<?>[]> methodParameterTypeMap = new LinkedHashMap<>();
        for (Method method : command.getCommandHandlerMethodMap().keySet()) {
            methodParameterTypeMap.put(method, method.getParameterTypes());
        }

        InvocableMethod invocableMethod = null;
        // (2) Create ParameterResolver, loop resolver
        ParameterResolver resolver = new ParameterResolver(commandSender);
        for (Map.Entry<Method, Class<?>[]> methodEntry : methodParameterTypeMap.entrySet()) {
            Class<?>[] parameters = methodEntry.getValue();
            Object[] args = resolver.resolver(parameters);
            try {
                // (3) Create invocableMethod
                invocableMethod = new InvocableMethod(command, methodEntry.getKey(), args);
            } catch (Exception ignore) {
            }
        }

        // (4) invoke
        if (invocableMethod != null) {
            invocableMethod.doInvoke();
        }
    }

    public void start(PluginWrapper pluginWrapper) {
        Set<AbstractCommand> commands = pluginWrapper.getCommands();
        for (AbstractCommand command : commands) {
            Map<Method, CommandPusher> commandPushMethodMap = command.getCommandPusherMethodMap();
            commandPushMethodMap.forEach((method, commandPusher) -> pluginWrapper.getPlugin().getPusherExecutorService().scheduleAtFixedRate(() -> {

                ParameterResolver resolver = new ParameterResolver(bot);
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

    public void stop(PluginWrapper pluginWrapper) {
        //  关闭推送器
        pluginWrapper.getPlugin().getPusherExecutorService().shutdown();
    }

    @Nullable
    public AbstractCommand matchCommand(PluginWrapper pluginWrapper, String commandName) {
        for (AbstractCommand command : pluginWrapper.getCommands()) {
            if (commandName.equals(commandPrefix + command.getPrimaryName())) {
                return command;
            }
        }
        return null;
    }


    @Nullable
    public AbstractCommand matchCommand(String commandName, AbstractCommand command) {
        for (AbstractCommand abstractCommand : command.getChildrenCommand()) {
            if (commandName.equals(abstractCommand.getPrimaryName()) || "*".equals(abstractCommand.getPrimaryName())) {
                return abstractCommand;
            }
        }
        return null;
    }

}
