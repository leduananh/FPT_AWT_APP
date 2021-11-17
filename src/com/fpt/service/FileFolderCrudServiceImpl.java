package com.fpt.service;

import com.fpt.folderHandleLib.FileFolderCrudLib;

import java.util.Scanner;
import java.util.regex.Pattern;

public class FileFolderCrudServiceImpl implements FileFolderCrudService {
    private final Scanner scanner = new Scanner(System.in);

    @Override
    public void createFileFolder() {
        String sourcePath = readText("input your sourcePath to create file/folder \n is relative path or absolute path included parent directory and file name with format extension: ");
        boolean isCreated = FileFolderCrudLib.createFileFolder(sourcePath);
        System.out.println("respond status: " + isCreated);
    }

    @Override
    public void moveFileFolder() {
        String sourcePath = readText("input your sourcePath to create file/folder \n is relative path or absolute path included parent directory and file name with format extension: ");
        String targetPath = readText("input your targetPath to create file/folder \n is relative path or absolute path included parent directory and file name with format extension: ");
        boolean isCreated = FileFolderCrudLib.moveFileFolder(sourcePath,targetPath);
        System.out.println("respond status: " + isCreated);
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
