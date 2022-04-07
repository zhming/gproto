package com.gproto.service.impl;

import com.google.common.collect.Maps;
import com.gproto.common.ProtobufProcessor;
import com.gproto.constants.ProtoConstant;
import com.gproto.entity.JsonTreeEntity;
import com.gproto.entity.ProtoInfoEntity;
import com.gproto.service.FileService;
import com.gproto.utils.FileUtil;
import io.swagger.annotations.Authorization;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.SystemUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.Map;
import java.util.logging.Logger;

@Service
public class FileServiceImpl implements FileService {
    private static final Logger log = Logger.getLogger(FileServiceImpl.class.getName());
    private static final String jarFlag = "[INFO] Building jar: ";
    @Value("${gproto.save-file-path}")
    private String basePath;
    @Value("${gproto.proto-jar-path}")
    private String protoJarPath;

    @Autowired
    private ProtobufProcessor protobufProcessor;

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

    private static ProtoInfoEntity getProtoInfo(String path) throws IOException {
        Map<String, String> keyWordMap = Maps.newHashMap();
        keyWordMap.put(ProtoConstant.JAVA_PACKAGE, null);
        keyWordMap.put(ProtoConstant.JAVA_OUTER_CLASSNAME, null);

        Map<String, String> valueMap = FileUtil.findContent(path, keyWordMap);

        ProtoInfoEntity protoInfoEntity = new ProtoInfoEntity();
        protoInfoEntity.setPackageName(valueMap.get(ProtoConstant.JAVA_PACKAGE));
        protoInfoEntity.setClassName(valueMap.get(ProtoConstant.JAVA_OUTER_CLASSNAME));
        return protoInfoEntity;
    }

    @Override
    public ProtoInfoEntity saveFile(String fileName, String fileContent, String uid) throws IOException {
        log.info("basePath" + basePath);
        log.info("protoJarPath" + protoJarPath);
        log.info(fileContent + "\n");
        String mavenGprotoPath = basePath + "/" + uid + "/maven-gproto";
        String path = basePath + "/" + uid;
        String fullFileName = path + "/maven-gproto/src/main/proto/proto/" + fileName;
        String jarName = fileName.substring(0, fileName.lastIndexOf("."));
        String[] cmd = {"cmd", "/C", mavenGprotoPath + "/maven-build.bat", jarName, " ", mavenGprotoPath};

        if (SystemUtils.IS_OS_LINUX) {
            path = basePath + "/" + uid;
            fullFileName = path + "/maven-gproto/src/main/proto/proto/" + fileName;
            mavenGprotoPath = basePath + "/" + uid + "/maven-gproto";
            log.info("mavenGprotoPath" + mavenGprotoPath);
            log.info("fullFileName" + fullFileName);
            jarName = fileName.substring(0, fileName.lastIndexOf("."));
            cmd = new String[]{mavenGprotoPath + "/maven-build.sh", jarName, " ", mavenGprotoPath};
        }

        initUserFolder(uid, path);

        save(fullFileName, fileContent);

        try {
            runMavenBuild(cmd, jarFlag);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ProtoInfoEntity result = getProtoInfo(fullFileName);

        return result;
    }

    @Override
    public boolean clearFile(String fileName, String uid) throws IOException {
        //TODO:
        return false;
    }

    @Override
    public ProtoInfoEntity storeFile(String fileName, String uid, MultipartFile file) throws IOException {
        String path = basePath + "/" + uid;
        String fullFileName = path + "/maven-gproto/src/main/proto/proto/" + fileName;
        String mavenGprotoPath = basePath + "/" + uid + "/maven-gproto";
        String jarName = fileName.substring(0, fileName.lastIndexOf("."));

        String[] cmd = {"cmd", "/C", mavenGprotoPath + "/maven-build.bat", jarName, " ", mavenGprotoPath};

        if (SystemUtils.IS_OS_LINUX) {
            path = basePath + "/" + uid;
            fullFileName = path + "/maven-gproto/src/main/proto/proto/" + fileName;
            mavenGprotoPath = basePath + "/" + uid + "/maven-gproto";
            jarName = fileName.substring(0, fileName.lastIndexOf("."));
            cmd = new String[]{mavenGprotoPath + "/maven-build.sh", jarName, " ", mavenGprotoPath};
        }

        initUserFolder(uid, path);

        file.transferTo(new File(fullFileName));

        try {
            runMavenBuild(cmd, jarFlag);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ProtoInfoEntity result = getProtoInfo(fullFileName);

        return result;
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

    private void runMavenBuild(String[] cmd, String jarFlag) throws IOException, InterruptedException {
        if (SystemUtils.IS_OS_LINUX) {
            ProcessBuilder builder = new ProcessBuilder("/bin/chmod", "755", cmd[0]);

            Process process = builder.start();

            process.waitFor();
        }

        Process proc = Runtime.getRuntime().exec(cmd);
        String jarInfo = "";
        int ret = 99;
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


            ret = proc.waitFor();
            log.info("ret: " + ret);
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw e;
        }
        if (ret == 0) {
            String sourceJarDir = jarInfo.substring(jarFlag.length());
            log.info("sourceJarDir: " + sourceJarDir);
            File file = new File(sourceJarDir);
            String jarFileName = file.getName();
            log.info("jarFileName: " + jarFileName);
            copyFile(sourceJarDir, protoJarPath + "/" + jarFileName);
        }else {
            throw new InterruptedException("build fail");
        }


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
