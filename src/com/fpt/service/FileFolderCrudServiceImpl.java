package com.fpt.service;

import com.fpt.folderHandleLib.FileFolderCrudLib;

import java.util.Scanner;
import java.util.regex.Pattern;

public class FileFolderCrudServiceImpl implements FileFolderCrudService {
    private final Scanner scanner = new Scanner(System.in);

    @Override
    public void createFileFolder() {
        String sourcePath = readText("input your sourcePath to create file/folder \n is absolute path or relative path include parent directory and file name starting from project root: ");
        boolean isCreated = FileFolderCrudLib.createFileFolder(sourcePath);
        System.out.println("respond status: " + isCreated);
    }

    @Override
    public void moveFileFolder() {
        String sourcePath = readText("input your sourcePath to create file/folder \n is absolute path or relative path include parent directory and file name starting from project root: ");
        String targetPath = readText("input your targetPath to create file/folder \n is absolute path or relative path include parent directory and file name starting from project root: ");
        boolean isMoved = FileFolderCrudLib.moveFileFolder(sourcePath,targetPath);
        System.out.println("respond status: " + isMoved);
    }

    @Override
    public void checkFileFolderExist() {
        String sourcePath = readText("input your sourcePath to create file/folder \n is absolute path or relative path include parent directory and file name starting from project root: ");
        boolean isExist = FileFolderCrudLib.checkFileFolderExists(sourcePath);
        System.out.println("respond status: " + isExist);
    }

    private String readText(String request) {
        String input = null;
        while (true) {
            try {
                printRequest(request);
                input = scanner.nextLine();
                if (input.isEmpty())
                    throw new Exception("do not empty...");
                else if (isNumeric(input))
                    throw new Exception("only text...");
                break;
            } catch (Exception e) {
                System.out.println("error: " + e.getMessage());
            }
        }
        return input;
    }

    private void printRequest(String request) {
        System.out.print("+ " + request);
    }

    private boolean isNumeric(String strNum) {
        Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");
        if (strNum == null)
            return false;
        return pattern.matcher(strNum).matches();
    }
}
