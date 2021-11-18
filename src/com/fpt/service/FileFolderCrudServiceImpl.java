package com.fpt.service;

import com.fpt.folderHandleLib.FileFolderCrudLib;

import java.util.Scanner;
import java.util.Set;
import java.util.regex.Pattern;

public class FileFolderCrudServiceImpl implements FileFolderCrudService {
    private final Scanner scanner = new Scanner(System.in);

    @Override
    public void createFileFolder() {
        String sourcePath = readText("input your sourcePath file/folder \n is absolute path or relative path include parent directory and file name starting from project root to create: ");
        boolean isCreated = FileFolderCrudLib.createFileFolder(sourcePath);
        System.out.println("respond status: " + isCreated);
    }

    @Override
    public void moveFileFolder() {
        String sourcePath = readText("input your sourcePath file/folder \n is absolute path or relative path include parent directory and file name starting from project root: ");
        String targetPath = readText("input your targetPath file/folder \n is absolute path or relative path include parent directory and file name starting from project root: ");
        boolean isMoved = FileFolderCrudLib.moveFileFolder(sourcePath, targetPath);
        System.out.println("respond status: " + isMoved);
    }

    @Override
    public void checkFileFolderExist() {
        String sourcePath = readText("input your sourcePath file/folder \n is absolute path or relative path include parent directory and file name starting from project root to checking: ");
        boolean isExist = FileFolderCrudLib.checkFileFolderExists(sourcePath);
        System.out.println("respond status: " + isExist);
    }

    @Override
    public void renameFileFolder() {
        String sourcePath = readText("input your sourcePath file/folder \n is absolute path or relative path include parent directory and file name starting from project root to rename: ");
        String newName = readText("input your new file/folder name: ");
        boolean isRenamed = FileFolderCrudLib.renameFileFolder(sourcePath, newName);
        System.out.println("respond status: " + isRenamed);
    }

    @Override
    public void listFileFolderNames() {
        String sourcePath = readText("input your sourcePath file/folder \n is absolute path or relative path include parent directory and file name starting from project root to rename: ");
        Set<String> fileFolderNames = FileFolderCrudLib.listFileFolderNames(sourcePath);
        fileFolderNames.forEach(name -> System.out.println("+ " + name));
    }

    @Override
    public void listFileNames() {
        String sourcePath = readText("input your sourcePath file/folder \n is absolute path or relative path include parent directory and file name starting from project root to rename: ");
        Set<String> fileNames = FileFolderCrudLib.listFileNames(sourcePath);
        fileNames.forEach(name -> System.out.println("+ " + name));
    }

    @Override
    public void listFolderNames() {
        String sourcePath = readText("input your sourcePath file/folder \n is absolute path or relative path include parent directory and file name starting from project root to rename: ");
        Set<String> FolderNames = FileFolderCrudLib.listFolderNames(sourcePath);
        FolderNames.forEach(name -> System.out.println("+ " + name));
    }

    @Override
    public void listFilesDetail() {

    }

    @Override
    public void deleteFileFolder() {
        String sourcePath = readText("input your sourcePath to delete file/folder \n is absolute path or relative path include parent directory and file name starting from project root: ");
        boolean isRenamed = FileFolderCrudLib.deleteFileFolder(sourcePath);
        System.out.println("respond status: " + isRenamed);
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
