package com.fpt;

import com.fpt.app.ConsoleApp;
import com.fpt.enumType.AppConfig;

public class Main {

    public static void main(String[] args) {
        ConsoleApp consoleApp = new ConsoleApp("\n" + AppConfig.APP_TITLE.getConfigValue());
        consoleApp.start();
    }
}
