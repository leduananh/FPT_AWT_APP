package com.fpt.app;

public enum AppFunctionsEnum {
    CREATE_FILE_FOLDER("createFileFolder",  "createFileFolder createFileFolder",   true),
    MOVE_FILE_FOLDER     ("moveFileFolder",       "moveFileFolder moveFileFolder",   true),
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
