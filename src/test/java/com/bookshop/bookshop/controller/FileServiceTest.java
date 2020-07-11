package com.bookshop.bookshop.controller;

import com.bookshop.bookshop.model.DBFile;

import com.bookshop.bookshop.model.Story;
import com.bookshop.bookshop.model.Topic;
import com.bookshop.bookshop.model.User;
import com.bookshop.bookshop.repository.DBFileRepository;

import com.bookshop.bookshop.security.UserPrincipal;
import com.bookshop.bookshop.service.DBFileStorageService;
import com.bookshop.bookshop.service.implementation.DBFileStorageServiceImpl;

import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;


import java.util.Optional;

import static ch.qos.logback.core.encoder.ByteArrayUtil.hexStringToByteArray;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(MockitoJUnitRunner.class)
public class FileServiceTest {

    DBFileRepository dbFileRepository = Mockito.mock(DBFileRepository.class);
    DBFileStorageService dbFileStorageService = new DBFileStorageServiceImpl(dbFileRepository);
 

    @Test
    public void should_return_store_avatar_method() throws Exception {

        User user = new User();
        user.setId(344l);
        user.setUsername("hosu794");
        user.setPassword("password");
        user.setName("Grzegorz Szczęsny");
        user.setEmail("hosu794@gmail.com");

        UserPrincipal userPrincipal = UserPrincipal.create(user);

        byte[] CDRIVES = hexStringToByteArray("e04fd020ea3a6910a2d808002b30309d");
        DBFile dbFile = new DBFile("Example Filename", "image", CDRIVES);

        MockMultipartFile firstFile = new MockMultipartFile("data", "filename.txt", "text/plain", "some xml".getBytes());

        Mockito.when(dbFileRepository.existsByFilename(ArgumentMatchers.anyString())).thenReturn(true);
        Mockito.when(dbFileRepository.findByFilename(ArgumentMatchers.anyString())).thenReturn(Optional.of(dbFile));
        Mockito.when(dbFileRepository.save(ArgumentMatchers.any())).thenReturn(dbFile);


        Assert.assertEquals(dbFile.getFilename(), dbFileStorageService.storeAvatar(firstFile, userPrincipal).getFilename());
        Assert.assertEquals(dbFile.getFileType(), dbFileStorageService.storeAvatar(firstFile, userPrincipal).getFileType());
        Assert.assertNotNull(dbFileStorageService.storeAvatar(firstFile, userPrincipal).getData());


    }

    @Test
    public void should_return_getFileById() throws Exception {

        byte[] CDRIVES = hexStringToByteArray("e04fd020ea3a6910a2d808002b30309d");
        DBFile dbFile = new DBFile("Example Filename", "image", CDRIVES);
        Mockito.when(dbFileRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(dbFile));

        Assert.assertEquals(dbFile.getData(), dbFileStorageService.getFileById(434l).getData());
    }

    @Test
    public void should_return_getFileByFilename() throws Exception {
        byte[] CDRIVES = hexStringToByteArray("e04fd020ea3a6910a2d808002b30309d");
        DBFile dbFile = new DBFile("Example Filename", "image", CDRIVES);

        Mockito.when(dbFileRepository.findByFilename(ArgumentMatchers.anyString())).thenReturn(Optional.of(dbFile));

        Assert.assertEquals(dbFile.getData(), dbFileStorageService.getFileByFilename("Some Filename").getData());
        Assert.assertEquals(dbFile.getFilename(), dbFileStorageService.getFileByFilename("Some Filename").getFilename());
    }

    


}
