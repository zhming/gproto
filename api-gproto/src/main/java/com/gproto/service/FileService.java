package com.gproto.service;

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
    boolean saveFile(String fileName, String fileContent, String uid) throws IOException;

    boolean clearFile(String fileName, String uid)  throws IOException;
}
