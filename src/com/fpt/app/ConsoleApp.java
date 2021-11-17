package com.fpt.app;

public class ConsoleApp {
    private Menu appMenu;
    private String APP_TITLE;
    public ConsoleApp(String title) {
        this.APP_TITLE = title;
        this.appMenu = new Menu(APP_TITLE);
    }

    public void start(){
//        AppFunctionsEnum.values()
        this.appMenu.show();
    }


}
