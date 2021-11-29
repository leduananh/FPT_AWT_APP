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

    public void fileFolderAttributes() throws IOException;

    public void fileFolderCreationDate() throws FileNotFoundException, IOException;

    public void fileFolderLastModifiedDate() throws FileNotFoundException, IOException;

    public void fileFolderLastAccessDate() throws IOException;

    public void overWriteFileData() throws IOException;

    public void fileHasKeyword() throws IOException;

    public void fileDataAtRowIndex() throws IOException;

    public void fileReplaceAll() throws FileNotFoundException, IOException;

    public void deleteFileFolder() throws IOException;

    public void countToThenFocus();
}
