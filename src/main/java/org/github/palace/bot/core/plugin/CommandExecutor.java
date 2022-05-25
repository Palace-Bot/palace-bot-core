package org.github.palace.bot.core.plugin;

import net.mamoe.mirai.contact.MemberPermission;
import org.github.palace.bot.core.cli.CommandSender;
import org.github.palace.bot.core.cli.CommandSession;
import org.github.palace.bot.core.cli.support.InvocableMethod;
import org.github.palace.bot.core.cli.support.ParameterResolver;

import javax.annotation.Nullable;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author jihongyuan
 * @date 2022/5/23 10:36
 */
public class CommandExecutor {

    public void executeCommand(CommandSender commandSender, AbstractCommand command,  CommandSession session) {
        // (1) Get command method parameterTypes
        Map<Method, Class<?>[]> methodParameterTypeMap = new LinkedHashMap<>();
        for (Method method : command.getCommandHandlerMethodMap().keySet()) {
            methodParameterTypeMap.put(method, method.getParameterTypes());
        }

        InvocableMethod invocableMethod = null;
        // (2) Create ParameterResolver, loop resolver
        ParameterResolver resolver = new ParameterResolver(commandSender, session);
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
