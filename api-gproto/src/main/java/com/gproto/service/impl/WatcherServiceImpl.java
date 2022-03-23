package com.gproto.service.impl;

import com.gproto.classloader.DynamicJarLoader;
import com.gproto.common.PathUtil;
import com.gproto.common.ProtoJarAlterationMonitor;
import com.gproto.service.WatcherService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;

@Service
public class WatcherServiceImpl implements WatcherService {
    @Value("${gproto.proto-jar-path}")
    private String protoJarPath;

    @PostConstruct
    @Override
    public void watchDir() {
        new Thread(() -> {
            try {
                new ProtoJarAlterationMonitor(protoJarPath);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
        }).start();
        try {
            URL[] urls = PathUtil.getURLs(Paths.get(protoJarPath));
            DynamicJarLoader.getInstance().init(urls);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
