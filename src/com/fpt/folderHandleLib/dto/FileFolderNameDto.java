package com.fpt.folderHandleLib.dto;

public class FileFolderNameDto {
    private String name;
    private String path;
    private boolean isFile;
    private boolean isFolder;

    public FileFolderNameDto(String name, String path, boolean isFile, boolean isFolder) {
        this.name = name;
        this.path = path;
        this.isFile = isFile;
        this.isFolder = isFolder;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isFile() {
        return isFile;
    }

    public void setFile(boolean file) {
        isFile = file;
    }

    public boolean isFolder() {
        return isFolder;
    }

    public void setFolder(boolean folder) {
        isFolder = folder;
    }
}
