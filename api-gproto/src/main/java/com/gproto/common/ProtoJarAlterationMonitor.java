package com.gproto.common;


import com.gproto.classloader.DynamicJarLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.io.IOException;
import java.net.URL;
import java.nio.file.*;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 监控目标文件夹，如有变化（新增、更新、删除），将触发重新加载目标文件夹下的jar文件
 * @author qianzhm
 */
public class ProtoJarAlterationMonitor {
    private static final Logger log = LoggerFactory.getLogger(ProtoJarAlterationMonitor.class);
    private final Path path;
    private ThreadPoolTaskExecutor executor;

    public ProtoJarAlterationMonitor(String path) throws IOException, InterruptedException, InstantiationException, IllegalAccessException {
        this.path = Paths.get(path);
        initPool();
        register();
    }

    private void register() throws IOException, InterruptedException {
        URL[] urls = PathUtil.getURLs(this.path);
        WatchService watchService = FileSystems.getDefault().newWatchService();
        path.register(watchService,
                StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_MODIFY, StandardWatchEventKinds.ENTRY_DELETE);
        while (true) {
            WatchKey watchKey = watchService.take();
            for (WatchEvent event : watchKey.pollEvents()) {
                executor.execute(() -> {
                    DynamicJarLoader dynamicJarLoader = DynamicJarLoader.getInstance();
                    dynamicJarLoader.reInit(urls);
                    log.info("reload jar ok");
                });
            }
            watchKey.reset();
        }
    }


    private void initPool() {
        executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(20);
        executor.setKeepAliveSeconds(5);
        executor.setQueueCapacity(200);
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.initialize();
    }
}
