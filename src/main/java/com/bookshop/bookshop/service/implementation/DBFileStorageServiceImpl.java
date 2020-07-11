package com.bookshop.bookshop.service.implementation;

import com.bookshop.bookshop.exception.FileStorageException;
import com.bookshop.bookshop.exception.MyFileNotFoundException;
import com.bookshop.bookshop.exception.ResourceNotFoundException;
import com.bookshop.bookshop.model.DBFile;
import com.bookshop.bookshop.repository.DBFileRepository;
import com.bookshop.bookshop.security.UserPrincipal;
import com.bookshop.bookshop.service.DBFileStorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;


@Service
@Transactional
public class DBFileStorageServiceImpl implements DBFileStorageService {

    @Autowired
    public DBFileStorageServiceImpl(DBFileRepository dbFileRepository) {
        this.dbFileRepository = dbFileRepository;
    }

    final private static Logger logger = LoggerFactory.getLogger(DBFileStorageServiceImpl.class);

    final private DBFileRepository dbFileRepository;

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

    public DBFile getFileById(Long fileId) {
        return dbFileRepository.findById(fileId)
                .orElseThrow(() -> new MyFileNotFoundException("File not found with id " + fileId));
    }

    public DBFile getFileByFilename(String filename) {

        Optional<DBFile> foundFile = dbFileRepository.findByFilename(filename);

        boolean isAvatarExists = foundFile.isPresent();

        if(isAvatarExists) {
            return dbFileRepository.findByFilename(filename)
                    .orElseThrow(() -> new MyFileNotFoundException("File not found with id " + filename));
        } else {
            return null;
        }




    }

}
