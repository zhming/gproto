package com.gproto.service.impl;

import com.gproto.service.FileService;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.logging.Logger;

@Service
public class FileServiceImpl implements FileService {
    private static final Logger log = Logger.getLogger(FileServiceImpl.class.getName());
    @Value("${gproto.save-file-path}")
    private String basePath;

    @Override
    public boolean saveFile(String fileName, String fileContent, String uid) throws IOException {
        compile(fileName, "1111", fileContent);
        return true;
    }

    @Override
    public boolean clearFile(String fileName, String uid)  throws IOException {
        //TODO:
        return false;
    }

    private void compile(String fileName, String uid, String fileContent) throws IOException{
        //TODO
        //1. copy base maven source to current user dir
        File userDir = new File(basePath + "/" + uid );

        if(!userDir.exists()){
            userDir.mkdirs();
            // copy maven-gproto
            String sourceDir = basePath + "/maven-gproto";
            String destDir = basePath  + "/" + uid+ "/maven-gproto";
            copyDirectory(sourceDir, destDir);
        }


        //2. save proto file to current user maven dir
        save(uid,fileName, fileContent);

        //3. run maven-compile.bat
            //3-1. compile
            //3-2. cp jar to target dir
        Runtime runtime = Runtime.getRuntime();
        String mavenGprotoPath = basePath + "\\" + uid+  "\\maven-gproto";
        log.info(mavenGprotoPath + "\\maven-build.bat 1123 TestService");
        runtime.exec(mavenGprotoPath + "\\maven-build.bat 1123 TestService");
        String [] cmd={"cmd","/C","copy exe1 exe2"};
        Process proc =Runtime.getRuntime().exec(cmd);


    }

    private void save(String uid, String fileName, String fileContent) throws IOException{
        String path = basePath+ "/" + uid;
        String fullFileName = path + "/maven-gproto/src/main/proto/proto/" + fileName;
        RandomAccessFile stream = new RandomAccessFile(fullFileName, "rw");
        FileChannel channel = stream.getChannel();
        byte[] strBytes = fileContent.getBytes();
        ByteBuffer buffer = ByteBuffer.allocate(strBytes.length);
        buffer.put(strBytes);
        buffer.flip();
        channel.write(buffer);
        stream.close();
        channel.close();
    }

    public static void copyDirectory(String sourceDirectoryLocation, String destinationDirectoryLocation) throws IOException {
        File sourceDirectory = new File(sourceDirectoryLocation);
        File destinationDirectory = new File(destinationDirectoryLocation);
        FileUtils.copyDirectory(sourceDirectory, destinationDirectory);
    }
}
