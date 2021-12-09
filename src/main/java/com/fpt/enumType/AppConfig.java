package com.fpt.enumType;

public enum AppConfig {
    APP_TITLE("appTitle", "FILE FOLDER HANDLE LIBRARY CONSOLE APP", true),
    APP_TITLE_FOCUS("appTitleFocus", "MAIN", true),
    APP_FOCUS_BAT_PATH("appFocusBatName","D:\\x\\out\\artifacts\\x_jar\\focus.vbs",true),
    ;

    private final String configName;
    private final String configValue;
    private final boolean isUsed;

    private AppConfig(String configName, String configValue, boolean isUsed) {
        this.configName = configName;
        this.configValue = configValue;
        this.isUsed = isUsed;
    }

    public String getConfigName() {
        return configName;
    }

    public String getConfigValue() {
        return configValue;
    }

    public boolean isUsed() {
        return isUsed;
    }
}
