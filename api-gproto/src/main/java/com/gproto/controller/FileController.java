package com.gproto.controller;

import com.google.common.base.Strings;
import com.gproto.entity.ProtoUploadRequestEntity;
import com.gproto.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/gproto/v1/file")
public class FileController {
    @Autowired
    private FileService fileService;

    @PostMapping("/saveProto")
    public String saveProtoToFile(@RequestBody ProtoUploadRequestEntity requestData){
        if(Strings.isNullOrEmpty(requestData.getFileName()) || Strings.isNullOrEmpty(requestData.getContent()) || Strings.isNullOrEmpty(requestData.getUid())){
            return "error";
        }

        try {
            fileService.saveFile(requestData.getFileName(), requestData.getContent(), requestData.getUid());
        } catch (IOException e) {
            e.printStackTrace();
            return "error";
        }

        return "success";
    }

    @PostMapping("/uploadProto")
    public String uploadProtoToFile(@RequestParam("uid") String uid, @RequestParam("fileName")String fileName, @RequestParam("file") MultipartFile file){
        if(Strings.isNullOrEmpty(fileName) || Strings.isNullOrEmpty(uid)){
            return "error";
        }

        if(!fileName.endsWith(".proto")){
            return "error: not proto file";
        }

        try {
            fileService.storeFile(fileName, uid, file);
        } catch (IOException e) {
            e.printStackTrace();
            return "error";
        }

        return "success";
    }
}
