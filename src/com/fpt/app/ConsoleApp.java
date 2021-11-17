package com.fpt.app;

import java.util.Scanner;

public class ConsoleApp {
    private Menu appMenu;
    private String APP_TITLE;
    private final String REQUEST_INPUT_TITLE = "input function index to invoke: ";
    public ConsoleApp(String title) {
        this.APP_TITLE = title;
        this.appMenu = new Menu(APP_TITLE);
    }

    public void start(){
//        AppFunctionsEnum.values()
        this.appMenu.show();
    }
    private void readUserInput(){
        Scanner scanner = new Scanner(System.in);
        while(true){
            System.out.println(REQUEST_INPUT_TITLE);
            int index = scanner.nextInt();
            String functionName = this.appMenu.getFunctionNameByIndex(index);
        }

    }

}
