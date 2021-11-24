package com.fpt.service;

import java.io.FileNotFoundException;
import java.io.IOException;

public interface FileFolderCrudService {
    public void createFolder() throws IOException;

    public void moveFileFolder() throws IOException;

    public void checkFileFolderExist();

    public void renameFileFolder() throws IOException;

    public void listFileFolderNames() throws IOException;

    public void listFileNames() throws FileNotFoundException;

    public void listFolderNames() throws FileNotFoundException;

    public void listFileFolderAttributes() throws FileNotFoundException;

    public void mergeFileData() throws IOException;

    public void appendFileContent() throws IOException;

    public void readFileData() throws IOException;

    public void writeDataToFile() throws IOException;

    public void overWriteFile() throws IOException;

    public void fileFolderSize() throws FileNotFoundException;

    public void fileFolderAttributes() throws FileNotFoundException;

    public void fileFolderCreationDate() throws FileNotFoundException;

    public void fileFolderLastModifiedDate() throws FileNotFoundException;

    public void fileFolderLastAccessDate() throws FileNotFoundException;

    public void overWriteFileData();

    public void fileHasKeyword() throws FileNotFoundException;

    public void fileDataAtRowIndex() throws IOException;

    public void fileReplaceAll() throws FileNotFoundException;

    public void deleteFileFolder() throws IOException;
}
