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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;



@RestController
@RequestMapping("/api")
public class FileController {

    private static final Logger logger = LoggerFactory
            .getLogger(FileController.class);

    @Autowired
    public FileController(DBFileStorageServiceImpl dbFileStorageService) {
        this.dbFileStorageService = dbFileStorageService;
    }


    final private DBFileStorageServiceImpl dbFileStorageService;


    @PutMapping("/uploadAvatar")
    @PreAuthorize("hasRole('USER')")
    public UploadFileResponse uploadAvatar(@RequestParam("file") MultipartFile file, @CurrentUser UserPrincipal currentUser) {
        DBFile dbFile = dbFileStorageService.storeAvatar(file, currentUser);

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/downloadFile/")
                .path(String.valueOf(dbFile.getId()))
                .toUriString();

        String message = "Avatar update successfully";

        return new UploadFileResponse(currentUser.getId().toString(), fileDownloadUri, file.getContentType(), message);
    }

    @GetMapping("/downloadFile/{fileId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long fileId) {

        DBFile foundFile = dbFileStorageService.getFileById(fileId);

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
    @PreAuthorize("hasRole('USER')")
    public UploadFileResponse getDownloadLink(@CurrentUser UserPrincipal currentUser) {
        DBFile foundFile = dbFileStorageService.getFileByFilename(currentUser.getId().toString());

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/downloadFile/")
                .path(String.valueOf(foundFile.getId()))
                .toUriString();

        return new UploadFileResponse(currentUser.getId().toString(), fileDownloadUri, foundFile.getFileType());
    }

}