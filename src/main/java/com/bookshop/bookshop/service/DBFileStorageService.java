package com.bookshop.bookshop.service;

import com.bookshop.bookshop.model.DBFile;
import com.bookshop.bookshop.security.UserPrincipal;
import com.sun.org.apache.xpath.internal.operations.Mult;
import org.springframework.web.multipart.MultipartFile;

public interface DBFileStorageService {

    public DBFile storeAvatar(MultipartFile file, UserPrincipal currentUser);

    public DBFile getFileById(Long fileId);

    public DBFile getFileByFilename(String filename);

}
