package org.github.palace.bot.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * @author JHY
 * @date 2022/4/6 15:21
 */
@Slf4j
public final class ZipUtil {

    private ZipUtil() {
    }

    /**
     * 解压
     *
     * @param zipFilePath  带解压文件
     * @param desDirectory 解压到的目录
     */
    public static void unzip(String zipFilePath, String desDirectory) throws Exception {
        ZipUtil.unzip(new File(zipFilePath), desDirectory);
    }

    /**
     * 解压
     *
     * @param zipFilePath  带解压文件
     * @param desDirectory 解压到的目录
     */
    public static void unzip(File zipFilePath, String desDirectory) throws Exception {

        File desDir = new File(desDirectory);
        if (!desDir.exists()) {
            boolean mkdirSuccess = desDir.mkdir();
            if (!mkdirSuccess) {
                throw new Exception("创建解压目标文件夹失败");
            }
        }
        // 读入流
        try (ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(zipFilePath))) {
            ZipEntry zipEntry = zipInputStream.getNextEntry();
            while (zipEntry != null) {
                // 文件夹
                if (zipEntry.isDirectory()) {
                    String unzipFilePath = desDirectory + File.separator + zipEntry.getName();
                    // 直接创建
                    mkdir(new File(unzipFilePath));
                }
                // 文件
                else {
                    String unzipFilePath = desDirectory + File.separator + zipEntry.getName();
                    File file = new File(unzipFilePath);
                    // 创建父目录
                    mkdir(file.getParentFile());
                    // 写出文件流
                    try (BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(unzipFilePath))) {
                        byte[] bytes = new byte[1024];
                        int readLen;
                        while ((readLen = zipInputStream.read(bytes)) != -1) {
                            bufferedOutputStream.write(bytes, 0, readLen);
                        }
                    }
                }
                zipInputStream.closeEntry();
                zipEntry = zipInputStream.getNextEntry();
            }
        }
    }

    public static File[] getDirectoryChildFiles(String path) {
        File parentDirectory = new File(path);

        Map<String, File> fileMap = new HashMap<>(16);
        if (parentDirectory.canRead() && parentDirectory.isDirectory()) {
            File[] files = parentDirectory.listFiles();

            if (files == null || files.length == 0) {
                return new File[0];
            }

            for (File file : files) {
                String fileName = file.getName();

                if (fileName.endsWith(".jar")) {
                    fileMap.put(fileName.replaceAll(".jar", ""), file);
                } else if (!"lib".equals(fileName) && file.isDirectory() && file.canRead() && !fileMap.containsKey(fileName)) {
                    fileMap.put(fileName, file);
                }
            }
            return fileMap.values().toArray(new File[0]);
        }
        return new File[0];
    }

    /**
     * 获取资源文件
     *
     * @param resourceName 资源名称
     */
    public static URL[] getResources(String resourceName) {
        URL[] urls = new URL[0];

        File file = new File(resourceName);
        if (file.exists() && file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null && files.length > 0) {
                urls = new URL[files.length];
                for (int i = 0; i < files.length; i++) {
                    try {
                        urls[i] = files[i].toURI().normalize().toURL();
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return urls;
    }

    /**
     * 如果父目录不存在则创建
     */
    private static void mkdir(File file) {
        if (null == file || file.exists()) {
            return;
        }
        mkdir(file.getParentFile());
        file.mkdir();
    }


}