package com.fpt.enumType;

public enum AppFunctionsEnum {
    CREATE_FILE_FOLDER("createFileFolder", "create new file/folder from (path) with response status (undefined)", true),
    MOVE_FILE_FOLDER("moveFileFolder", "move file/folder from location (sourcePath) to destination location (targetPath) with response status (undefined)", true),
    CHECK_FILE_FOLDER_EXIST("checkFileFolderExist", "check file/folder from source location is exists with response status store in variable (undefined)", true),
    RENAME_FILE_FOLDER("renameFileFolder", "rename file/folder from location (path) to new (newName) with response status (undefined)", true),
    LIST_FILE_FOLDER_NAMES("listFileFolderNames", "get files and subdirectories name from file/folder location (sourcePath) with response data (undefined)", true),
    LIST_FILE_NAMES("listFileNames", "get files name from file/folder location (sourcePath) with response data (undefined)", true),
    LIST_FOLDER_NAMES("listFolderNames", "get subdirectories name from file/folder location (sourcePath) with response data (undefined)", true),
    LIST_FILES_DETAIL("listFilesDetail", "get files and subdirectories detail from file/folder location (sourcePath) with response status store in variable (undefined)", true),
    MERGE_FILE_DATA("mergeFileData", "merge file from file location (sourcePath) to target file location (targetPath) and status store in local variable (undefined)", true),
    APPEND_FILE_CONTENT("appendFileContent", "append new content (newContent) to source file (sourcePath) with response data (undefined)", true),
    READ_FILE_DATA("readFileData", "read data from source file (sourcePath) with file data response in variable (undefined)", true),
    WRITE_DATA_TO_FILE("writeDataToFile", "write data (data)  to source file (sourcePath) with response status in variable (undefined)", true),
    DELETE_FILE_FOLDER("deleteFileFolder", "delete file/folder from location (path) with response status (undefined)", false),
    ;

    private final String functionName;
    private final String functionDescription;
    private final boolean isUsed;

    private AppFunctionsEnum(String name, String description, boolean isUsed) {
        this.functionName = name;
        this.functionDescription = description;
        this.isUsed = isUsed;
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
