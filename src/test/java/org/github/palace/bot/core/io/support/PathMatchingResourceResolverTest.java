package org.github.palace.bot.core.io.support;

import org.junit.Test;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.IOException;
import java.util.Arrays;

/**
 * @author jihongyuan
 * @date 2022/9/15 9:18
 */
public class PathMatchingResourceResolverTest {

    public ResourcePatternResolver resourceResolver  = new PathMatchingResourcePatternResolver();

//    @Before
//    public void before(){
//        List<URL> urls = new ArrayList<>();
//        urls.addAll(Arrays.asList(ZipUtil.getResources("../plugins")));
//        urls.addAll(Arrays.asList(ZipUtil.getResources("../plugins" + "/lib")));
//        PluginClassLoader pluginClassLoader = new PluginClassLoader(getClass().getClassLoader(), urls.toArray(new URL[0]));
//        resourceResolver = new PathMatchingResourceResolver();
//    }

    @Test
    public void test1() throws IOException {
        Resource[] resources = resourceResolver.getResources("org/github/**/**.class");
        System.out.println(Arrays.toString(resources));
    }

}
