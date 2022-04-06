package com.gproto.service;

import com.gproto.entity.ProtoInfoEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 *  file service
 */
public interface FileService {
    /**
     *
     * @param fileName
     * @param fileContent
     * @param uid
     * @return
     * @throws IOException
     */
    ProtoInfoEntity saveFile(String fileName, String fileContent, String uid) throws IOException;

    boolean clearFile(String fileName, String uid)  throws IOException;

    ProtoInfoEntity storeFile(String fileName, String uid, MultipartFile file) throws IOException;
}
