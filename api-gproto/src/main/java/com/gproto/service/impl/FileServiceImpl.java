package com.gproto.service.impl;

import com.gproto.service.FileService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

@Service
public class FileServiceImpl implements FileService {
    @Value("${gproto.save-file-path}")
    private String basePath;

    @Override
    public boolean saveFile(String fileName, String fileContent, String uid) throws IOException {
        String path = basePath+ "/" + uid;
        String fullFileName = path + "/" + fileName;
        File dir = new File(path);
        if(!dir.exists()){
            dir.mkdirs();
        }
        RandomAccessFile stream = new RandomAccessFile(fullFileName, "rw");
        FileChannel channel = stream.getChannel();
        byte[] strBytes = fileContent.getBytes();
        ByteBuffer buffer = ByteBuffer.allocate(strBytes.length);
        buffer.put(strBytes);
        buffer.flip();
        channel.write(buffer);
        stream.close();
        channel.close();
        return true;
    }

    @Override
    public boolean clearFile(String fileName, String uid)  throws IOException {
        //TODO
        return false;
    }

    private void compile(File file){
        //TODO
        //1. copy base maven source to current user dir
        //2. save proto file to current user maven dir
        //3. run maven-compile.bat
            //3-1. compile
            //3-2. cp jar to target dir

    }
}
