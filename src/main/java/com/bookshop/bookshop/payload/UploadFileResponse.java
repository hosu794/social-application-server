package com.bookshop.bookshop.payload;

public class UploadFileResponse {

    private String filename;
    private String fileDownloadUri;
    private String fileType;


    public UploadFileResponse(String filename, String fileDownloadUri, String fileType) {
        this.filename = filename;
        this.fileDownloadUri = fileDownloadUri;
        this.fileType = fileType;

    }

    public UploadFileResponse() {}

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getFileDownloadUri() {
        return fileDownloadUri;
    }

    public void setFileDownloadUri(String fileDownloadUri) {
        this.fileDownloadUri = fileDownloadUri;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

}
