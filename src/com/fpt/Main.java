package com.fpt;

import com.fpt.app.ConsoleApp;
import com.fpt.enumType.AppConfig;
import com.fpt.folderHandleLib.FileSimplify;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.*;

public class Main {
    public static void moveFileOrFolder(Path sourcePath, Path destFolderPath) throws IOException {
        if (Files.notExists(sourcePath)) {
            final String message = sourcePath + " is not exist";
            throw new FileNotFoundException(message);
        }
        Files.createDirectories(destFolderPath);

        Files.move(sourcePath, destFolderPath.resolve(sourcePath.getFileName()),
                StandardCopyOption.REPLACE_EXISTING);
    }

    public static void main(String[] args) throws Exception {
        String root = "D:\\record\\New folder\\";
        Path srcPath = Paths.get(root + "New Text Document.txt");
        Path dstPath = Paths.get(root + "New Text Document3.txt");
        String test ="D:\\record\\New folder\\New folder";
String a = FileSimplify.getAllLineMatchContentJson("end",srcPath.toString());
        FileSimplify.overwriteFileContent(a,dstPath.toString());
//        System.out.println(Files.isRegularFile(path, LinkOption.NOFOLLOW_LINKS));
//        ConsoleApp consoleApp = new ConsoleApp("\n" + AppConfig.APP_TITLE.getConfigValue());
//        consoleApp.start();
    }
}
