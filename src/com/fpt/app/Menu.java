package com.fpt.app;

import com.fpt.enumType.AppFunctionsEnum;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Menu {
    private List<AppFunctionsEnum> functions;
    private String TITLE = "Console App";

    Menu() {
        functions = Arrays.stream(AppFunctionsEnum.values()).collect(Collectors.toList());
    }

    Menu(String title) {
        this.TITLE = title;
        functions = Arrays.stream(AppFunctionsEnum.values()).collect(Collectors.toList()).stream().filter(appFunctionsEnum -> appFunctionsEnum.isUsed()).collect(Collectors.toList());
    }

    public void show() {
        System.out.println(TITLE + "\n");

        for (int i = 0; i < functions.size(); i++) {
            int lineIndex = i + 1;
            String line = lineIndex + ". " + functions.get(i).getFunctionTitle();
            System.out.println(line);
        }
    }

    public String getFunctionNameByIndex(int index) {
        return functions.get(index).getFunctionName();
    }

    public int getFunctionSize() {
        return functions.size();
    }

}
