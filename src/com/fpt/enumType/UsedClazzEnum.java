package com.fpt.enumType;

public enum UsedClazzEnum {
    FILE_FOLDER_CRUD("FileFolderCrudService",  "com.fpt.service.AppFunctionServiceImpl",   true),
    ;

    private final String clazzName;
    private final String clazzPackage;
    private final boolean isUsed;

    private UsedClazzEnum(String clazzName, String clazzPackage, boolean isUsed) {
        this.clazzName = clazzName;
        this.clazzPackage = clazzPackage;
        this.isUsed = isUsed;
    }

    public String getClazzName() {
        return clazzName;
    }

    public String getClazzPackage() {
        return clazzPackage;
    }

    public boolean isUsed() {
        return isUsed;
    }
}
