package com.fpt.service;

public interface FileFolderCrudService {
    public void createFileFolder();

    public void moveFileFolder();

    public void checkFileFolderExist();

    public void renameFileFolder();

    public void listFileFolderNames();

    public void listFileNames();

    public void listFolderNames();

    public void listFileFolderAttributes();

    public void mergeFileData();

    public void appendFileContent();

    public void readFileData();

    public void writeDataToFile();

    public void overWriteFile();

    public void fileFolderSize();

    public void fileFolderAttributes();

    public void fileFolderCreationDate();

    public void fileFolderLastModifiedDate();

    public void fileFolderLastAccessDate();

    public void overWriteFileData();

    public void fileHasKeyword();

    public void fileDataAtRowIndex();

    public void fileReplaceAll();

    public void deleteFileFolder();
}
