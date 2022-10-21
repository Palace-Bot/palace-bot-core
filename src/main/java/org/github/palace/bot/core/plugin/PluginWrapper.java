package org.github.palace.bot.core.plugin;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.github.palace.bot.core.LifeCycle;
import org.github.palace.bot.core.annotation.Application;
import org.github.palace.bot.core.exception.PluginException;
import org.github.palace.bot.core.loader.PluginClassLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.TypeFilter;

import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * 插件包装器
 *
 * @author jihongyuan
 * @date 2022/5/12 16:48
 */
@Getter
@Slf4j
public class PluginWrapper implements LifeCycle {

    /**
     * plugin properties
     */
    private final PluginProperties properties;

    private final PluginClassLoader pluginLoader;

    private final CommandManager commandManager;

    @Getter
    private final ScheduledExecutorService pusherExecutorService = Executors.newSingleThreadScheduledExecutor();

    @Getter
    private Plugin plugin;

    // spring提供
    // 本来想自己写太难了，还是决定用spring

    /**
     * 资源解析器
     */
    private final ResourcePatternResolver resourceResolver;

    /**
     * 注解类型过滤器
     */
    private final List<TypeFilter> includeFilters = new ArrayList<>();

    /**
     * 元数据读取器工厂
     */
    private MetadataReaderFactory metadataReaderFactory;

    public PluginWrapper(PluginProperties properties, ResourcePatternResolver resourceResolver, PluginClassLoader pluginLoader) {
        this.properties = properties;
        this.pluginLoader = pluginLoader;
        this.resourceResolver = resourceResolver;
        this.commandManager = new CommandManager(properties.commandPrefix, this);

        // 注册过滤器
        registerDefaultFilter();
    }

    private void registerDefaultFilter() {
        this.includeFilters.add(new AnnotationTypeFilter(org.github.palace.bot.core.annotation.Command.class));
    }

    /**
     * Create Plugin
     */
    public synchronized Plugin createPlugin() {
        if (plugin == null) {
            try {
                Class<?> clazz = pluginLoader.loadClass(properties.mainClass);
                if (clazz.isAnnotationPresent(Application.class)) {
                    plugin = new PluginDelegate(clazz.getAnnotation(Application.class), clazz);
                } else if (Plugin.class.isAssignableFrom(clazz)) {
                    plugin = (Plugin) clazz.getDeclaredConstructor().newInstance();
                } else {
                    throw new PluginException("Plugin main class must extends from Plugin or annotated with @Application");
                }
                plugin.onLoad();
                onLoadInternal();
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
        return plugin;
    }

    @Override
    public void start() {
        for (AbstractCommand command : plugin.getCommands()) {
            commandManager.addCommand(command);
        }
        commandManager.start();
    }

    @Override
    public void stop() {
        pusherExecutorService.shutdown();
        commandManager.stop();
    }


    /**
     * load base package class
     */
    private void onLoadInternal() {
        String[] scanBasePackages = plugin.getScanBasePackages();

        if(scanBasePackages == null || scanBasePackages.length == 0) return;

        // 加载 包扫描路径
        Set<Resource> resources = new LinkedHashSet<>();
        for (String scanBasePackage : scanBasePackages) {
            try {
                resources.addAll(List.of(resourceResolver.getResources(scanBasePackage)));
            } catch (IOException e) {
                if (log.isDebugEnabled()) {
                    log.debug("scans: ", e);
                }
            }
        }

        Set<MetadataReader> candidates = new LinkedHashSet<>();
        for (Resource resource : resources) {
            for (TypeFilter tf : this.includeFilters) {
                try {
                    MetadataReader metadataReader = getMetadataReaderFactory().getMetadataReader(resource);
                    if (tf.match(metadataReader, getMetadataReaderFactory())) {
                        candidates.add(metadataReader);
                    }
                } catch (IOException e) {
                    if (log.isDebugEnabled()) {
                        log.debug("error: ", e);
                    }
                }

            }
        }

        // 类加载器
        for (MetadataReader candidate : candidates) {
            try {
                Class<?> clazz = pluginLoader.loadClass(candidate.getClassMetadata().getClassName());
                plugin.register(new CommandDelegate(clazz.getAnnotation(org.github.palace.bot.core.annotation.Command.class), clazz));
            } catch (ClassNotFoundException e) {
                if (log.isDebugEnabled()) {
                    log.debug("register: ", e);
                }
            }

        }
    }

    /**
     * Return the MetadataReaderFactory used by this component provider.
     */
    public final MetadataReaderFactory getMetadataReaderFactory() {
        if (this.metadataReaderFactory == null) {
            this.metadataReaderFactory = new CachingMetadataReaderFactory();
        }
        return this.metadataReaderFactory;
    }

}
