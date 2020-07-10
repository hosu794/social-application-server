package com.bookshop.bookshop.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "files")
public class DBFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;


    private String filename;
    
    private String fileType;

    @Lob
    private byte[] data;

    public DBFile() {

    }


    public DBFile(long id, String filename, String fileType, byte[] data) {
        this.id = id;
        this.filename = filename;
        this.fileType = fileType;
        this.data = data;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public DBFile(String filename, String fileType, byte[] data) {
        this.filename = filename;
        this.fileType = fileType;
        this.data = data;
    }



    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}
