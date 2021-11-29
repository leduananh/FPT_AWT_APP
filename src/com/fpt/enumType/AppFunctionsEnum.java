package com.fpt.enumType;

public enum AppFunctionsEnum {
    CREATE_FILE_FOLDER(1, "createFolder", "create new file/directories from [1] with response status store in variable [boolean result];", true),
    MOVE_FILE_FOLDER(2, "moveFileFolder", "move file/directory from source path [1] to destination location [2] with response status store in variable [boolean result];", true),
    CHECK_FILE_FOLDER_EXIST(3, "checkFileFolderExist", "check file/directory from source location [1] is exists with response status store in variable [boolean result];", true),
    RENAME_FILE_FOLDER(4, "renameFileFolder", "rename file/directory from source location [1] to new name [2] with response status store in variable [boolean result];", true),
    LIST_FILE_FOLDER_NAMES(5, "listFileFolderNames", "list files and subdirectories name from source location [1] with response data store in variable [jsonStringArray result];", true),
    LIST_FILE_NAMES(6, "listFileNames", "list files name from source location [1] with response data store in variable [jsonStringArray result];", true),
    LIST_FOLDER_NAMES(7, "listFolderNames", "list subdirectories name from source location [1] with response data store in variable [jsonStringArray result];", true),
    MERGE_FILE_DATA(8, "mergeFileData", "merge file from source file location [1] to target file location [2] and response status store in local variable [boolean result];", true),
    APPEND_FILE_CONTENT(9, "appendFileContent", "append new content [1] to exist source file [2] with response status store in local variable [boolean result];", true),
    READ_FILE_DATA(10, "readFileData", "read data from source path [1] with response data store in variable [text result];", true),
    WRITE_DATA_TO_FILE(11, "writeDataToFile", "write data [1] to source file [2] , create file if not exist with response status store in local variable [boolean result];", true),
    OVER_WRITE_FILE(12, "overWriteFileData", "override new data (data) to file location (filePath) with response data in variable (undefined)", true),
    FILE_FOLDER_SIZE(13, "fileFolderSize", "get file/folder size from [1] with response data store in variable [text result];", true),
    FILE_FOLDER_ATTRIBUTES(14, "fileFolderAttributes", "get attributes from file/directory path [1] with response data store in variable [jsonObject result];", true),
    LIST_FILE_FOLDER_ATTRIBUTES(15, "listFileFolderAttributes", "list files and subdirectories detail from directory source location [1] with response data store in variable [jsonObjectArray result];", true),
    FILE_FOLDER_CREATION_DATE(16, "fileFolderCreationDate", "get created date time from file/directory path [1] with response data store in variable [text result];", true),
    FILE_FOLDER_LAST_MODIFIED_DATE(17, "fileFolderLastModifiedDate", "get last modified date time from file/directory path [1] with response data store in variable [text result];", true),
    FILE_FOLDER_LAST_ACCESS_DATE(18, "fileFolderLastAccessDate", "get last accessed date time from file/directory path [1] with response data store in variable [text result];", true),
    FILE_HAS_KEY_WORD(19, "fileHasKeyword", "checking keyword [1] is exists in file path [2] with response status store in local variable [boolean result];", true),
    FILE_DATA_AT_ROW_INDEX(20, "fileDataAtRowIndex", "get file data at row index [1] from source file [2] with response data row store in local variable [text result];", true),
    FILE_REPLACE_ALL(21, "fileReplaceAll", "replace keyword [1] with new keyword [2] from file path [3] with response data store in local variable [text result];", true),
    DELETE_FILE_FOLDER(22, "deleteFileFolder", "delete file/directory from source location path [1] with response data store in variable [boolean result];", false),
    COUNT_TO_THEN_GAIN_FOCUS(23,"countToThenFocus","count to number then gain focus back to app", true),
    OPEN_AND_KILL_PROCESS(24,"openAndKillProcess","open and kill process", true);
    ;

    private final int index;
    private final String functionName;
    private final String functionTitle;
    private final boolean isUsed;

    private AppFunctionsEnum(int index, String name, String title, boolean isUsed) {
        this.index = index;
        this.functionName = name;
        this.functionTitle = title;
        this.isUsed = isUsed;
    }

    public int getIndex() {
        return index;
    }

    public String getFunctionName() {
        return functionName;
    }

    public String getFunctionTitle() {
        return functionTitle;
    }

    public boolean isUsed() {
        return isUsed;
    }
}
