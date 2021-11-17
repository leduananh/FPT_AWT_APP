package com.fpt.enumType;

public enum AppFunctionsEnum {
    CREATE_FILE_FOLDER("createFileFolder", "create new file/folder from (path) with response status (undefined)", true),
    MOVE_FILE_FOLDER("moveFileFolder", "move file/folder from location (sourcePath) to destination location (targetPath) with response status (undefined)", true),
    CHECK_FILE_FOLDER_EXIST("checkFileFolderExists", "check file/folder from source location is exists with response status store in variable (undefined)", true),

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
