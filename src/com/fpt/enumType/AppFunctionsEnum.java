package com.fpt.enumType;

public enum AppFunctionsEnum {
    CREATE_FILE_FOLDER(1, "createFileFolder", "create new file/folder from (path) with response status (undefined)", true),
    MOVE_FILE_FOLDER(2, "moveFileFolder", "move file/folder from location (sourcePath) to destination location (targetPath) with response status (undefined)", true),
    CHECK_FILE_FOLDER_EXIST(3, "checkFileFolderExist", "check file/folder from source location is exists with response status store in variable (undefined)", true),
    RENAME_FILE_FOLDER(4, "renameFileFolder", "rename file/folder from location (path) to new (newName) with response status (undefined)", true),
    LIST_FILE_FOLDER_NAMES(5, "listFileFolderNames", "get files and subdirectories name from file/folder location (sourcePath) with response data (undefined)", true),
    LIST_FILE_NAMES(6, "listFileNames", "get files name from file/folder location (sourcePath) with response data (undefined)", true),
    LIST_FOLDER_NAMES(7, "listFolderNames", "get subdirectories name from file/folder location (sourcePath) with response data (undefined)", true),
    MERGE_FILE_DATA(8, "mergeFileData", "merge file from file location (sourcePath) to target file location (targetPath) and status store in local variable (undefined)", true),
    APPEND_FILE_CONTENT(9, "appendFileContent", "append new content (newContent) to source file (sourcePath) with response data (undefined)", true),
    READ_FILE_DATA(10, "readFileData", "read data from source file (sourcePath) with file data response in variable (undefined)", true),
    WRITE_DATA_TO_FILE(11, "writeDataToFile", "write data (data)  to source file (sourcePath) with response status in variable (undefined)", true),
    OVER_WRITE_FILE(12, "overWriteFileData", "override new data (data) to file location (filePath) with response data in variable (undefined)", true),
    FILE_FOLDER_SIZE(13, "getFileFolderSize", "get file/folder size from (sourcePath) with size store in variable (undefined)", true),
    FILE_FOLDER_ATTRIBUTES(14, "fileFolderAttributes", "get attributes from file/folder (path) with attribute store in variable (undefined)", true),
    LIST_FILE_FOLDER_ATTRIBUTES(15, "listFileFolderAttributes", "get files and subdirectories detail from file/folder location (sourcePath) with response status store in variable (undefined)", true),
    FILE_FOLDER_CREATION_DATE(16, "fileFolderCreationDate", "get creation date from file/folder (path) with date store in variable (undefined)", true),
    FILE_FOLDER_LAST_MODIFIED_DATE(17, "fileFolderLastModifiedDate", "get last modified date from file/folder (path) with date store in variable (undefined)", true),
    FILE_FOLDER_LAST_ACCESS_DATE(18, "fileFolderLastAccessDate", "get last accessed date from file/folder (path) with date store in variable (undefined)", true),
    DELETE_FILE_FOLDER(19, "deleteFileFolder", "delete file/folder from location (path) with response status (undefined)", false),
    ;

    private final int index;
    private final String functionName;
    private final String functionDescription;
    private final boolean isUsed;

    private AppFunctionsEnum(int index, String name, String description, boolean isUsed) {
        this.index = index;
        this.functionName = name;
        this.functionDescription = description;
        this.isUsed = isUsed;
    }

    public int getIndex() {
        return index;
    }

    public String getFunctionName() {
        return functionName;
    }

    public String getFunctionDescription() {
        return functionDescription;
    }

    public boolean isUsed() {
        return isUsed;
    }
}
