package com.gproto.service.impl;

import com.gproto.service.FileService;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.SystemUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.logging.Logger;

@Service
public class FileServiceImpl implements FileService {
    private static final Logger log = Logger.getLogger(FileServiceImpl.class.getName());
    private static final String jarFlag = "[INFO] Building jar: ";
    @Value("${gproto.save-file-path}")
    private String basePath;
    @Value("${gproto.proto-jar-path}")
    private String protoJarPath;

    public static void copyDirectory(String sourceDirectoryLocation, String destinationDirectoryLocation) throws IOException {
        File sourceDirectory = new File(sourceDirectoryLocation);
        File destinationDirectory = new File(destinationDirectoryLocation);
        FileUtils.copyDirectory(sourceDirectory, destinationDirectory);
    }

    public static void copyFile(String sourceDirectoryLocation, String destinationDirectoryLocation) throws IOException {
        File sourceDirectory = new File(sourceDirectoryLocation);
        File destinationDirectory = new File(destinationDirectoryLocation);
        FileUtils.copyFile(sourceDirectory, destinationDirectory);
    }

    @Override
    public boolean saveFile(String fileName, String fileContent, String uid) throws IOException {
        log.info(fileContent + "\n");
        String mavenGprotoPath = basePath + "\\" + uid + "\\maven-gproto";
        String path = basePath + "/" + uid;
        String fullFileName = path + "/maven-gproto/src/main/proto/proto/" + fileName;
        String jarName = fileName.substring(0, fileName.lastIndexOf("."));
        String[] cmd = {"cmd", "/C", mavenGprotoPath + "\\maven-build.bat", jarName, " ", mavenGprotoPath};

        if(SystemUtils.IS_OS_LINUX){
            path = basePath + "/" + uid;
            fullFileName = path + "/maven-gproto/src/main/proto/proto/" + fileName;
            mavenGprotoPath = basePath + "/" + uid + "/maven-gproto";
            jarName = fileName.substring(0, fileName.lastIndexOf("."));
            cmd = new String[]{"bash", "/C", mavenGprotoPath + "/maven-build.sh", jarName, " ", mavenGprotoPath};
        }

        initUserFolder(uid, path);

        save(fullFileName, fileContent);

        runMavenBuild(cmd, jarFlag);

        return true;
    }

    @Override
    public boolean clearFile(String fileName, String uid) throws IOException {
        //TODO:
        return false;
    }

    @Override
    public boolean storeFile(String fileName, String uid, MultipartFile file) throws IOException {
        String path = basePath + "/" + uid;
        String fullFileName = path + "/maven-gproto/src/main/proto/proto/" + fileName;
        String mavenGprotoPath = basePath + "\\" + uid + "\\maven-gproto";
        String jarName = fileName.substring(0, fileName.lastIndexOf("."));

        String[] cmd = {"cmd", "/C", mavenGprotoPath + "\\maven-build.bat", jarName, " ", mavenGprotoPath};

        if(SystemUtils.IS_OS_LINUX){
             path = basePath + "/" + uid;
             fullFileName = path + "/maven-gproto/src/main/proto/proto/" + fileName;
             mavenGprotoPath = basePath + "/" + uid + "/maven-gproto";
            jarName = fileName.substring(0, fileName.lastIndexOf("."));
            cmd = new String[]{"bash", "/C", mavenGprotoPath + "/maven-build.sh", jarName, " ", mavenGprotoPath};
        }

        initUserFolder(uid, path);

        file.transferTo(new File(fullFileName));

        runMavenBuild(cmd, jarFlag);
        return true;
    }

    private void initUserFolder(String uid, String path) throws IOException {
        File userDir = new File(basePath + "/" + uid);

        if (!userDir.exists()) {
            userDir.mkdirs();
            // copy maven-gproto
            String sourceDir = basePath + "/maven-gproto";
            String destDir = path + "/maven-gproto";
            copyDirectory(sourceDir, destDir);
        }
    }

    private void runMavenBuild(String[] cmd, String jarFlag) throws IOException {
        Process proc = Runtime.getRuntime().exec(cmd);
        String jarInfo = "";

        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    proc.getInputStream()));
            String line = null;
            while ((line = in.readLine()) != null) {
                if (line.startsWith(jarFlag)) {
                    jarInfo = line;
                }
                log.info(line + "\n");
            }


            int ret = proc.waitFor();
            log.info("ret: " + ret);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String sourceJarDir = jarInfo.substring(jarFlag.length());
        log.info("sourceJarDir: " + sourceJarDir);
        String jarFileName = sourceJarDir.substring(sourceJarDir.lastIndexOf("\\") + 1);
        log.info("jarFileName: " + jarFileName);
        copyFile(sourceJarDir, protoJarPath + "\\" + jarFileName);
    }

    private void save(String fileName, String fileContent) throws IOException {
        FileWriter fw = null;
        File file = new File(fileName);
        try {
            fw = new FileWriter(file, false);
            fw.write(fileContent + "\r");
        } catch (IOException e) {
            throw e;
        } finally {
            if (fw != null) {
                fw.close();
            }
        }

    }
}
