package com.bookshop.bookshop.service.implementation;

import com.bookshop.bookshop.exception.FileStorageException;
import com.bookshop.bookshop.exception.MyFileNotFoundException;
import com.bookshop.bookshop.exception.ResourceNotFoundException;
import com.bookshop.bookshop.model.DBFile;
import com.bookshop.bookshop.repository.DBFileRepository;
import com.bookshop.bookshop.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@Service
@Transactional
public class DBFileStorageServiceImpl {

    @Autowired
    private DBFileRepository dbFileRepository;



    public DBFile storeAvatar(MultipartFile file, UserPrincipal currentUser) {

        boolean isAvatarExists = dbFileRepository.existsByFilename(currentUser.getId().toString());

        if(isAvatarExists) {

            try {
              DBFile currentUserAvatar = dbFileRepository.findByFilename(currentUser.getId().toString())
                      .orElseThrow(() -> new ResourceNotFoundException("Avatar", "filename", currentUser.getId().toString()));


                currentUserAvatar.setData(file.getBytes());

                return dbFileRepository.save(currentUserAvatar);
            } catch (IOException ex) {
                throw new FileStorageException("Could not store file " + currentUser.getId().toString() + ". Please try again", ex);
            }

        } else {

            String filename = currentUser.getId().toString();
            try {
                DBFile newFile = new DBFile(filename, file.getContentType(), file.getBytes());

                return dbFileRepository.save(newFile);
            }  catch (IOException ex) {
                throw new FileStorageException("Could not store file " + filename + ". Please try again!", ex);
            }

        }

    }

    public DBFile getFile(Long fileId) {
        return dbFileRepository.findById(fileId)
                .orElseThrow(() -> new MyFileNotFoundException("File not found with id " + fileId));
    }

    public DBFile getFileByFilename(String filename) {
        return dbFileRepository.findByFilename(filename)
                .orElseThrow(() -> new MyFileNotFoundException("File not found with id" + filename));
    }

}
