package com.gproto.common;

import java.io.IOException;
import java.net.URL;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class PathUtil {

    private static  final Logger log = Logger.getLogger(PathUtil.class.getName());

    public static URL[] getURLs(Path path) throws IOException {
        // 加载固定位置文件夹下的所有文件
        if (path.toFile().isFile()) {
            return null;
        }
        List<Path> paths = new ArrayList<>();
        Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                if (file.toString().endsWith(".jar")) {
                    paths.add(file);
                }
                return FileVisitResult.CONTINUE;
            }
        });
        URL[] urls = new URL[paths.size()];
        // 加载的是jar包。需要循环每一个加载内部的class文件
        for (int i = 0; i < paths.size(); i++) {
            log.info("file: " + paths.get(i).toFile().getAbsolutePath());
            urls[i] = new URL("file:" + paths.get(i).toFile().getAbsolutePath());
        }

        return urls;
    }

}
