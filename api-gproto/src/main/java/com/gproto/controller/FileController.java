package com.gproto.controller;

import com.google.common.base.Strings;
import com.gproto.entity.ProtoInfoEntity;
import com.gproto.entity.ProtoUploadRequestEntity;
import com.gproto.entity.ResponseEntity;
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
    public ResponseEntity saveProtoToFile(@RequestBody ProtoUploadRequestEntity requestData) {
        if (Strings.isNullOrEmpty(requestData.getFileName()) || Strings.isNullOrEmpty(requestData.getContent()) || Strings.isNullOrEmpty(requestData.getUid())) {
            return ResponseEntity.respErrorInstance("10097");
        }

        try {
            ProtoInfoEntity protoInfoEntity = fileService.saveFile(requestData.getFileName(), requestData.getContent(), requestData.getUid());
            return ResponseEntity.respSuccess(protoInfoEntity);

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.respErrorInstance("10099");
        }

    }

    @PostMapping("/uploadProto")
    public ResponseEntity uploadProtoToFile(@RequestParam("uid") String uid, @RequestParam("fileName") String fileName, @RequestParam("file") MultipartFile file) {
        if (Strings.isNullOrEmpty(fileName) || Strings.isNullOrEmpty(uid)) {
            return ResponseEntity.respErrorInstance("10097");
        }

        if (!fileName.endsWith(".proto")) {
            return ResponseEntity.respErrorInstance("10098");
        }

        try {
            ProtoInfoEntity protoInfoEntity = fileService.storeFile(fileName, uid, file);

            return ResponseEntity.respSuccess(protoInfoEntity);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.respErrorInstance("10099");
        }

    }
}
