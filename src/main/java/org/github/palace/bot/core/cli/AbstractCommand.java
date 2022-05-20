package org.github.palace.bot.core.cli;

import lombok.Getter;
import net.mamoe.mirai.contact.MemberPermission;
import org.github.palace.bot.core.annotation.ChildCommandHandler;
import org.github.palace.bot.core.annotation.CommandHandler;
import org.github.palace.bot.core.annotation.CommandPusher;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @author JHY3
 * @date 2022/3/31 15:44
 */
public abstract class AbstractCommand extends Command {

    /**
     * 命令处理器
     */
    @Getter
    private final Map<Method, CommandHandler> commandHandlerMethodMap = new LinkedHashMap<>(16);

    /**
     * 推送器
     */
    @Getter
    private final Map<Method, CommandPusher> commandPusherMethodMap = new HashMap<>(16);

    /**
     * 子命令
     */
    @Getter
    private final List<AbstractCommand> childrenCommand = new ArrayList<>();

    protected AbstractCommand(String primaryName, String description) {
        super(primaryName, MemberPermission.MEMBER, false, description);
    }

    protected AbstractCommand(String primaryName, MemberPermission permission, String description) {
        super(primaryName, permission, false, description);
    }

    protected AbstractCommand(String primaryName, MemberPermission permission, boolean determine, String description) {
        super(primaryName, permission, determine, description);
    }

    /**
     * 解析 命令处理器和推送器
     */
    public void parse() {
        Method[] methods = this.getClass().getDeclaredMethods();
        if (methods.length > 0) {
            for (Method method : methods) {
                if (method.isAnnotationPresent(CommandHandler.class)) {
                    commandHandlerMethodMap.put(method, method.getAnnotation(CommandHandler.class));
                }
                // 解析子命令
                else if (method.isAnnotationPresent(ChildCommandHandler.class)) {
                    ChildCommandHandler childCommandHandler = method.getAnnotation(ChildCommandHandler.class);
                    try {
                        // TODO 待优化, 可以考虑字节码代理
                        Constructor<? extends AbstractCommand> constructor = this.getClass().getDeclaredConstructor();
                        AbstractCommand command = constructor.newInstance();
                        command.setPrimaryName(childCommandHandler.primaryName());
                        command.setPermission(this.getPermission());
                        command.setDetermine(false);
                        command.setDescription(null);

                        command.putCommandHandler(method, null);
                        childrenCommand.add(command);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                // 推送器
                else if (method.isAnnotationPresent(CommandPusher.class)) {
                    commandPusherMethodMap.put(method, method.getAnnotation(CommandPusher.class));
                }

            }
        }
    }


    /**
     * 注册命令
     */
    public void register(AbstractCommand command) {
        command.parse();
        childrenCommand.add(command);
    }

    public boolean hasChildrenCommand() {
        return !childrenCommand.isEmpty();
    }

    public void putCommandHandler(Method method, CommandHandler commandHandler) {
        this.commandHandlerMethodMap.put(method, commandHandler);
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

}