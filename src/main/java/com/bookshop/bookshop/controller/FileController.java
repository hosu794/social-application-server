package com.bookshop.bookshop.controller;


import com.bookshop.bookshop.model.DBFile;
import com.bookshop.bookshop.payload.UploadFileResponse;
import com.bookshop.bookshop.security.CurrentUser;
import com.bookshop.bookshop.security.UserPrincipal;
import com.bookshop.bookshop.service.implementation.DBFileStorageServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@RestController
public class FileController {

    private static final Logger logger = LoggerFactory
            .getLogger(FileController.class);

    @Autowired
    private DBFileStorageServiceImpl dbFileStorageService;

    @PostMapping("/uploadFile")
    public UploadFileResponse uploadFile(@RequestParam("file") MultipartFile file) {
        DBFile dbFile = dbFileStorageService.storeFile(file);

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/downloadFile/")
                .path(String.valueOf(dbFile.getId()))
                .toUriString();

        return new UploadFileResponse(dbFile.getFilename(), fileDownloadUri,
                file.getContentType());
    }

    @PostMapping("/uploadMultipleFiles")
    public List<UploadFileResponse> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files) {
        return Arrays.asList(files)
                .stream()
                .map(file -> uploadFile(file))
                .collect(Collectors.toList());
    }

    @PutMapping("/uploadAvatar")
    public UploadFileResponse uploadAvatar(@RequestParam("file") MultipartFile file, @CurrentUser UserPrincipal currentUser) {
        DBFile dbFile = dbFileStorageService.storeAvatar(file, currentUser);



        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/downloadFile/")
                .path(String.valueOf(dbFile.getId()))
                .toUriString();

        return new UploadFileResponse(currentUser.getId().toString(), fileDownloadUri, file.getContentType());
    }

    @GetMapping("/downloadFile/{fileId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long fileId) {
        // Load file from database
        DBFile foundFile = dbFileStorageService.getFile(fileId);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(foundFile.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + foundFile.getFilename() + "\"")
                .body(new ByteArrayResource(foundFile.getData()));
    }


    @GetMapping("/downloadFile/name/{filename}")
    public ResponseEntity<Resource> downloadFileByFilename(@PathVariable String filename) {
        DBFile foundFile = dbFileStorageService.getFileByFilename(filename);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(foundFile.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + foundFile.getFilename() + "\"")
                .body(new ByteArrayResource(foundFile.getData()));


    }


    @GetMapping("/user/avatar")
    public UploadFileResponse getDownloadLink(@CurrentUser UserPrincipal currentUser) {
        DBFile foundFile = dbFileStorageService.getFileByFilename(currentUser.getId().toString());

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/downloadFile/")
                .path(String.valueOf(foundFile.getId()))
                .toUriString();

        return new UploadFileResponse(currentUser.getId().toString(), fileDownloadUri, foundFile.getFileType());
    }

}