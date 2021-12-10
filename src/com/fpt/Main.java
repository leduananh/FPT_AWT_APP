package com.fpt;

import com.fpt.app.ConsoleApp;
import com.fpt.enumType.AppConfig;
import com.fpt.folderHandleLib.FileSimplify;

import java.nio.file.*;

public class Main {

    public static void main(String[] args) throws Exception {
        String root = "D:\\record\\New folder\\";
        Path srcPath = Paths.get("D:\\IMT_TOOLS\\jre-7u80-windows-x64.tar.gz");
        Path dstPath = Paths.get("D:\\record\\cc\\aa.txt");
        String test = "D:\\record\\New folder\\New folder";
//      String a  =  FileSimplify.getListFileAndFolderNames(dstPath.toString());

          FileSimplify.moveListFiles("D:\\record\\cc\\aa.txt","D:\\record");
//        System.out.println(Files.isRegularFile(path, LinkOption.NOFOLLOW_LINKS));
//        ConsoleApp consoleApp = new ConsoleApp("\n" + AppConfig.APP_TITLE.getConfigValue());
//        consoleApp.start();
    }
}
