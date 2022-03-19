package com.gproto.classloader;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.logging.Logger;

public class DynamicJarLoader {
    private static final Logger log = Logger.getLogger(DynamicJarLoader.class.getName());
    private URLClassLoader urlClassLoader;

    private static final DynamicJarLoader instance = new DynamicJarLoader();

    private DynamicJarLoader() {
    }

    public static DynamicJarLoader getInstance() {
        return DynamicJarLoader.instance;
    }

    public URLClassLoader getClassLoader() {
        return urlClassLoader;
    }

    public URLClassLoader init(URL[] urls) {
        if (null == this.urlClassLoader) {
            this.urlClassLoader = new URLClassLoader(urls);
        }
        return this.urlClassLoader;
    }

    public URLClassLoader reInit(URL[] urls) {
        if(null == urls || urls.length == 0){
            log.warning("urls is null or empty");
            return this.urlClassLoader;
        }
        try {
            if (null != this.urlClassLoader) {
                this.urlClassLoader.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.urlClassLoader = new URLClassLoader(urls);

        return this.urlClassLoader;

    }


}
