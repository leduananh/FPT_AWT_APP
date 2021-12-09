package com.fpt.folderHandleLib;

import javax.management.DescriptorKey;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;

public class Action {
    @DescriptorKey("Prefix:ART; "
            + "Move file/folder [1] to destination folder path [2], will overwrite existing folder and create if not exist; "
            + "sourcePath - Other - is relative/absolute path to file/folder with file format; "
            + "destFolderPath - Other - is destination relative/absolute path to folder; "
            + "Pass the test if the file/folder is moved successfully, otherwise the system will give an error message if it failed;")
    public void moveFileAndFolder(String sourcePath, String destFolderPath) throws Exception {
        FileSimplify.moveFileOrFolder(sourcePath,destFolderPath);
    }

    @DescriptorKey("Prefix:ART; "
            + "copy file/folder from path [1] to path to destination folder [2], create destination folder if not exists; "
            + "sourcePath - Other - is relative/absolute path to folder/file with file format; "
            + "destFolderPath - Other - is destination relative/absolute path to folder; "
            + "Pass the test if file/folder was copied successfully, otherwise the system will give an error message if it failed;")
    public void copyFileOrFolder(String filePath, String destFolderPath) throws Exception {
        FileSimplify.copyFileOrFolder(filePath,destFolderPath);
    }

    @DescriptorKey("Prefix:ART; "
            + "Create new folder from path to folder [1]; "
            + "folderPath - Other - is the absolute path to folder; "
            + "Pass the test if folder was created successfully, otherwise the system will give an error message if it failed;")
    public void createFolder(String sourcePath) throws Exception {
        FileSimplify.createFolder(sourcePath);
    }

    @DescriptorKey("Prefix:ART; "
            + "Check if the path to file/folder [1] exists; "
            + "sourcePath - Other - is relative/absolute path to folder/file with file format; "
            + "returns true if path to file/folder exists, false otherwise;")
    public boolean checkFileOrFolderIsExists(String sourcePath) throws Exception {
        return FileSimplify.isExists(sourcePath);
    }

    @DescriptorKey("Prefix:ART; "
            + "Check if the path to file/folder [1] is hidden; "
            + "sourcePath - Other - is relative/absolute path to file/folder; "
            + "Returns true if file/folder is hidden, false otherwise;")
    public boolean checkFileOrFolderIsHidden(String sourcePath) throws Exception {
        return FileSimplify.isHidden(sourcePath);
    }

    @DescriptorKey("Prefix:ART; "
            + "Get list of file/folder names from path to folder [1]; "
            + "folderPath - Other - is relative/absolute path to folder; "
            + "Returns list of file/folder names in json format, with json schema: [{\"name\": String, \"path\": String, \"isFile\": boolean, \"isFolder\": boolean},...];")
    public String getListFileAndFolderNames(String folderPath) throws Exception {
        return FileSimplify.getListFileAndFolderNames(folderPath);
    }

    @DescriptorKey("Prefix:ART; "
            + "Get list file names from path to folder [1]; "
            + "folderPath - Other - is relative/absolute path to folder; "
            + "Returns list of file names in json format, with json schema: [{\"name\": String, \"path\": String, \"isFile\": boolean, \"isFolder\": boolean},...];")
    public String getListFileNames(String folderPath) throws Exception {
        return FileSimplify.getListFileNames(folderPath);
    }

    @DescriptorKey("Prefix:ART; "
            + "Merge content from path to source file [1] into path to destination file [2]; "
            + "filePath - File - is fileName/absolute path to file with file format; "
            + "destFilePath - File - is fileName/absolute path to destination file with file format; "
            + "Pass the test if file has been merged successfully, otherwise the system will give an error message if it failed;")
    public void mergeFileContent(String filePath, String destFilePath) throws Exception {
        FileSimplify.mergeFileContent(filePath, destFilePath);
    }

    @DescriptorKey("Prefix:ART; "
            + "Append new content [1] into path to file [2], will create file if not exist; "
            + "content - Other - is new content; "
            + "filePath - File - is fileName/absolute path to file with file format; "
            + "Pass the test if the file has been successfully concatenated with the new content, otherwise the system will give an error message if it failed;")
    public void appendContentIntoFile(String content, String filePath) throws Exception {
        FileSimplify.appendContentIntoFile(content, filePath);
    }

    @DescriptorKey("Prefix:ART; "
            + "Overwrite content [1] from path to file [2], create file if not exist; "
            + "content - Other - is new content; "
            + "filePath - File - is fileName/absolute path to file with file format; "
            + "Pass the test if file has been overwritten successfully with the new content, otherwise the system will give an error message if it failed;")
    public void overwriteFileContent(String content, String filePath) throws Exception {
        FileSimplify.overwriteFileContent(content, filePath);
    }

    @DescriptorKey("Prefix:ART; "
            + "List folder names from path to folder [1]; "
            + "folderPath - Other - is relative/absolute path to folder; "
            + "Returns list of folder names in json format, with json schema: [{\"name\": String, \"path\": String, \"isFile\": boolean, \"isFolder\": boolean},...];")
    public String getListFolderNames(String folderPath) throws Exception {
        return FileSimplify.getListFolderNames(folderPath);
    }

    @DescriptorKey("Prefix:ART; "
            + "Read content from path to file [1]; "
            + "filePath - File - is fileName/absolute path to file with file format; "
            + "Returns the file content in plain text;")
    public String readFileData(String filePath) throws Exception {
        return FileSimplify.readFileData(filePath);
    }

    @DescriptorKey("Prefix:ART; "
            + "Get readable size from path to file/folder [1]; "
            + "sourcePath - Other - is relative/absolute path to folder/file with file format; "
            + "Returns the size of file/folder in readable text, exp: 5 Kib, 5 Mib, 5 Gib,...;")
    public String getFileOrFolderSizeReadable(String sourcePath) throws Exception {
        return FileSimplify.getFileOrFolderSizeReadable(sourcePath);
    }

    @DescriptorKey("Prefix:ART; "
            + "Get attributes detail from path to file/folder [1]; "
            + "sourcePath - Other - is relative/absolute path to folder/file with file format; "
            + "Returns file/folder attributes in json format, with json schema: "
            + "{\"name\": String, \"path\": String, \"sizeByte\": Long, \"sizeReadable\": String, \"createDateTime\": String, \"lastModifiedDateTime\": String, \"isFile\": boolean, \"isFolder\": boolean, \"isOther\": boolean, \"isSymbolicLink\": boolean, \"isHidden\": boolean };")
    public String getFileOrFolderAttributes(String sourcePath) throws Exception {
        return FileSimplify.getFileOrFolderAttributes(sourcePath);
    }

    @DescriptorKey("Prefix:ART; "
            + "List files and folder attributes detail from path to folder [1]; "
            + "folderPath - Other - is relative/absolute path to folder;"
            + "Returns list of file/folder attributes in json format, with json schema: "
            + "[{\"name\": String, \"path\": String, \"sizeByte\": Long, \"sizeReadable\": String, \"createDateTime\": String, \"lastModifiedDateTime\": String, \"isFile\": boolean, \"isFolder\": boolean, \"isOther\": boolean, \"isSymbolicLink\": boolean, \"isHidden\": boolean }];")
    public String getListFileAndFolderAttributes(String folderPath) throws Exception {
        return FileSimplify.getListFileAndFolderAttributes(folderPath);
    }

    @DescriptorKey("Prefix:ART; "
            + "Get created date time from path to file/folder [1]; "
            + "sourcePath - Other - is relative/absolute path to folder/file with file format; "
            + "Returns file/folder creation date in plain text, format: dd/MM/yyyy hh:mm:ss")
    public String getFileOrFolderCreationDate(String sourcePath) throws Exception {
        return FileSimplify.getFileOrFolderCreationDate(sourcePath);
    }

    @DescriptorKey("Prefix:ART; "
            + "Get last modified date time from path to file/folder [1]; "
            + "sourcePath - Other - is relative/absolute path to folder/file with file format; "
            + "Returns file/folder last modified date in plain text, format: dd/MM/yyyy hh:mm:ss")
    public String getFileFolderLastModifiedDate(String sourcePath) throws Exception {
        return FileSimplify.getFileFolderLastModifiedDate(sourcePath);
    }

    @DescriptorKey("Prefix:ART; "
            + "Get last accessed date time from path to file/folder [1]; "
            + "sourcePath - Other - is relative/absolute path to folder/file with file format; "
            + "Returns file/folder last access date in plain text, format: dd/MM/yyyy hh:mm:ss")
    public String getFileFolderLastAccessDate(String sourcePath) throws Exception {
        return FileSimplify.getFileFolderLastAccessDate(sourcePath);
    }

    @DescriptorKey("Prefix:ART; "
            + "Get line at row index [1] from path to file [2]; "
            + "index - Other - is row index (number); "
            + "filePath - File - is fileName/absolute path to file with file format; "
            + "Returns line content at row index in plain text;")
    public String getLineAtRowIndex(int rowIndex, String filePath) throws Exception {
        return FileSimplify.getLineAtRowIndex(rowIndex, filePath);
    }

    @DescriptorKey("Prefix:ART; "
            + "Get all line match with content [1] from path to file [2]; "
            + "content - Other - is lookup content; "
            + "filePath - File - is fileName/absolute path to file with file format; "
            + "Returns all the line match with search content in plain text;")
    public String getAllLineMatchContent(String content, String filePath) throws Exception {
        return FileSimplify.getAllLineMatchContent(content, filePath);
    }

    @DescriptorKey("Prefix:ART; "
            + "Get all line match with content [1] from path to file [2] in json format; "
            + "content - Other - is lookup content; "
            + "filePath - File - is fileName/absolute path to file with file format; "
            + "Returns all the line match with content in json format, json schema: {\"line\": String, \"lineIndex\": Integer};")
    public String getAllLineMatchContentJson(String content, String filePath) throws Exception {
        return FileSimplify.getAllLineMatchContentJson(content, filePath);
    }

    @DescriptorKey("Prefix:ART; "
            + "Replace all old content [1] with new content [2] from path to file [3], case sensitive; "
            + "oldContent - Other - is search content; "
            + "newContent - Other - is replace content; "
            + "filePath - File - is fileName/absolute path to file with file format; "
            + "Pass the test if the file has been successfully replaced with the new content, otherwise the system will give an error message if it failed;")
    public void fileReplaceAllContent(String oldContent, String newContent, String filePath) throws Exception {
        FileSimplify.replaceAllFileContent(oldContent, newContent, filePath);
    }

    @DescriptorKey("Prefix:ART; "
            + "Delete path to file/folder [1]; "
            + "sourcePath - Other - is relative/absolute path to folder/file with file format; "
            + "Pass the test if the file/folder was successfully deleted, otherwise the system will give an error message if it failed;")
    public void deleteFileOrFolder(String sourcePath) throws Exception {
        FileSimplify.deleteFileOrFolder(sourcePath);
    }
}
