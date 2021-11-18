package com.fpt.folderHandleLib.dto;

import java.io.Serializable;

public class FileFolderAttributesDto implements Serializable {
    private String name;
    private String sizeText;
    private long sizeByte;
    private String creationDateTime;
    private String lastAccessDateTime;
    private String lastModifiedDateTime;
    private boolean isFile;
    private boolean isDirectory;
    private boolean isOther;
    private boolean isSymbolicLink;

    public FileFolderAttributesDto() {
    }

    public FileFolderAttributesDto(String name, String sizeText, long sizeByte, String creationDateTime, String lastAccessDateTime, String lastModifiedDateTime, boolean isFile, boolean isDirectory, boolean isOther, boolean isSymbolicLink) {
        this.name = name;
        this.sizeText = sizeText;
        this.sizeByte = sizeByte;
        this.creationDateTime = creationDateTime;
        this.lastAccessDateTime = lastAccessDateTime;
        this.lastModifiedDateTime = lastModifiedDateTime;
        this.isFile = isFile;
        this.isDirectory = isDirectory;
        this.isOther = isOther;
        this.isSymbolicLink = isSymbolicLink;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSizeText() {
        return sizeText;
    }

    public void setSizeText(String sizeText) {
        this.sizeText = sizeText;
    }

    public long getSizeByte() {
        return sizeByte;
    }

    public void setSizeByte(long sizeByte) {
        this.sizeByte = sizeByte;
    }

    public String getCreationDateTime() {
        return creationDateTime;
    }

    public void setCreationDateTime(String creationDateTime) {
        this.creationDateTime = creationDateTime;
    }

    public String getLastAccessDateTime() {
        return lastAccessDateTime;
    }

    public void setLastAccessDateTime(String lastAccessDateTime) {
        this.lastAccessDateTime = lastAccessDateTime;
    }

    public String getLastModifiedDateTime() {
        return lastModifiedDateTime;
    }

    public void setLastModifiedDateTime(String lastModifiedDateTime) {
        this.lastModifiedDateTime = lastModifiedDateTime;
    }

    public boolean isFile() {
        return isFile;
    }

    public void setFile(boolean file) {
        isFile = file;
    }

    public boolean isDirectory() {
        return isDirectory;
    }

    public void setDirectory(boolean directory) {
        isDirectory = directory;
    }

    public boolean isOther() {
        return isOther;
    }

    public void setOther(boolean other) {
        isOther = other;
    }

    public boolean isSymbolicLink() {
        return isSymbolicLink;
    }

    public void setSymbolicLink(boolean symbolicLink) {
        isSymbolicLink = symbolicLink;
    }
}
