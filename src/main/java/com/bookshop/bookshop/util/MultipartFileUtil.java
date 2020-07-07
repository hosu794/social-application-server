package com.bookshop.bookshop.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class MultipartFileUtil {

    public static MultipartFile generateMultipartWithNewName(MultipartFile file, String filename) {
        return new MultipartFile() {
            @Override
            public String getName() {
                return file.getName();
            }

            @Override
            public String getOriginalFilename() {
                return filename;
            }

            @Override
            public String getContentType() {
                return file.getContentType();
            }

            @Override
            public boolean isEmpty() {
                return file.isEmpty();
            }

            @Override
            public long getSize() {
                return file.getSize();
            }

            @Override
            public byte[] getBytes() throws IOException {
                return file.getBytes();
            }

            @Override
            public InputStream getInputStream() throws IOException {
                return file.getInputStream();
            }

            @Override
            public void transferTo(File dest) throws IOException, IllegalStateException {
                file.transferTo(dest);
            }
        };
    }

}
