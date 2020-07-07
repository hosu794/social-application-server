package com.bookshop.bookshop.controller;

import com.bookshop.bookshop.exception.ResourceNotFoundException;
import com.bookshop.bookshop.model.User;
import com.bookshop.bookshop.payload.ApiResponse;
import com.bookshop.bookshop.payload.FileInfo;
import com.bookshop.bookshop.repository.UserRepository;
import com.bookshop.bookshop.security.CurrentUser;
import com.bookshop.bookshop.security.UserPrincipal;
import com.bookshop.bookshop.service.implementation.FilesStorageServiceImpl;
import com.bookshop.bookshop.util.MultipartFileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import javax.swing.plaf.ColorUIResource;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/api/files")
@CrossOrigin("http://localhost:8080")
public class FilesController {

    @Autowired
    FilesController(FilesStorageServiceImpl filesStorageService, UserRepository userRepository) {
        this.storageService = filesStorageService;
        this.userRepository = userRepository;
    }
    private final FilesStorageServiceImpl storageService;
    private final UserRepository userRepository;

    private static final Logger logger = LoggerFactory.getLogger(FilesController.class);

    @PostMapping("/upload")
    public ResponseEntity<ApiResponse> uploadFile(@RequestParam("file")MultipartFile file) {
        String message;


        try {
            storageService.save(file);

            message = "Uploaded the file successfully: " + file.getOriginalFilename();
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(true, message));

        } catch (Exception ex) {
            message = "Could not upload the file" + file.getOriginalFilename() + "!";
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ApiResponse(false, message));
        }
    }

    @PutMapping("/upload/avatar")
    public ResponseEntity<ApiResponse> uploadAvatar(@RequestParam("file") MultipartFile file, @CurrentUser UserPrincipal currentUser) {
        String message;

        User user = userRepository.findById(currentUser.getId())
            .orElseThrow(() -> new ResourceNotFoundException("User", "id", currentUser.getId()));

        String currentDate = new SimpleDateFormat("yyyyMMddHHmm").format(new Date()).toString();

        MultipartFile fileWithGeneratedName = MultipartFileUtil.generateMultipartWithNewName(file, currentDate + new Random().nextLong() + file.getOriginalFilename());

        try {
            storageService.save(fileWithGeneratedName);
            message = "Uploaded avatar successfully " + fileWithGeneratedName.getOriginalFilename();

            String url = MvcUriComponentsBuilder.fromMethodName(FilesController.class, "getFile", fileWithGeneratedName.getOriginalFilename().toString()).build().toString();

            ApiResponse responseImageMessage = new ApiResponse(true, message);
            user.setAvatar(url);
            userRepository.save(user);

            return ResponseEntity.status(HttpStatus.OK).body(responseImageMessage);

        } catch (Exception ex) {
            message = "Could not upload file: " + fileWithGeneratedName.getOriginalFilename() + " !";
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ApiResponse(false, message));

        }



    }

    @GetMapping
    public ResponseEntity<List<FileInfo>> getListFiles() {
        List<FileInfo> fileInfos = storageService.loadAll().map(path -> {
            String filename = path.getFileName().toString();
            String url = MvcUriComponentsBuilder.fromMethodName(FilesController.class, "getFile", path.getFileName().toString()).build().toString();

            return new FileInfo(filename, url);
        }).collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(fileInfos);
    }

    @GetMapping("/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> getFile(@PathVariable String filename) {
        Resource file = storageService.load(filename);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

}
