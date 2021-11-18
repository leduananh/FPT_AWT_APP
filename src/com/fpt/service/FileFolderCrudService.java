package com.fpt.service;

public interface FileFolderCrudService {
    public void createFileFolder();
    public void moveFileFolder();
    public void checkFileFolderExist();
    public void renameFileFolder();
    public void listFileFolderNames();
    public void listFileNames();
    public void listFolderNames();
    public void listFilesDetail();
    public void mergeFileData();
    public void appendFileContent();
    public void readFileData();
    public void writeDataToFile();
    public void overWriteFile();
    public void fileFolderSize();
    public void fileFolderAttributes();
    public void deleteFileFolder();
}
