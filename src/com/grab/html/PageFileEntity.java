package com.grab.html;

import java.io.File;

/**
 * Created by bqw on 14-3-25.
 */
public class PageFileEntity {
    private String fileName;
    private String pageUrl;
    private String filePath;
    private String uri;
    private File file;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getPageUrl() {
        return pageUrl;
    }

    public void setPageUrl(String pageUrl) {
        this.pageUrl = pageUrl;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getCurrentUrl() {
        return pageUrl.replaceAll(fileName, "");
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }
}
