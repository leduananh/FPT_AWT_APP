package com.fpt.service;

import com.fpt.folderHandleLib.FileFolderLib;

import java.util.Scanner;
import java.util.Set;
import java.util.regex.Pattern;

public class FileFolderCrudServiceImpl implements FileFolderCrudService {
    private final Scanner scanner = new Scanner(System.in);

    @Override
    public void createFileFolder() {
        String sourcePath = readText("input your sourcePath file/folder \n is absolute path or relative path include parent directory and file name starting from project root to create: ");
        boolean isCreated = FileFolderLib.createFileFolder(sourcePath);
        System.out.println("respond status: " + isCreated);
    }

    @Override
    public void moveFileFolder() {
        String sourcePath = readText("input your sourcePath file/folder \n is absolute path or relative path include parent directory and file name starting from project root: ");
        String targetPath = readText("input your targetPath file/folder \n is absolute path or relative path include parent directory and file name starting from project root: ");
        boolean isMoved = FileFolderLib.moveFileFolder(sourcePath, targetPath);
        System.out.println("respond status: " + isMoved);
    }

    @Override
    public void checkFileFolderExist() {
        String sourcePath = readText("input your sourcePath file/folder \n is absolute path or relative path include parent directory and file name starting from project root to checking: ");
        boolean isExist = FileFolderLib.checkFileFolderExists(sourcePath);
        System.out.println("respond status: " + isExist);
    }

    @Override
    public void renameFileFolder() {
        String sourcePath = readText("input your sourcePath file/folder \n is absolute path or relative path include parent directory and file name starting from project root to rename: ");
        String newName = readText("input your new file/folder name: ");
        boolean isRenamed = FileFolderLib.renameFileFolder(sourcePath, newName);
        System.out.println("respond status: " + isRenamed);
    }

    @Override
    public void listFileFolderNames() {
        String sourcePath = readText("input your sourcePath file/folder \n is absolute path or relative path include parent directory and file name starting from project root to rename: ");
        Set<String> fileFolderNames = FileFolderLib.listFileFolderNames(sourcePath);
        fileFolderNames.forEach(name -> System.out.println("+ " + name));
    }

    @Override
    public void listFileNames() {
        String sourcePath = readText("input your sourcePath file/folder \n is absolute path or relative path include parent directory and file name starting from project root to rename: ");
        Set<String> fileNames = FileFolderLib.listFileNames(sourcePath);
        fileNames.forEach(name -> System.out.println("+ " + name));
    }

    @Override
    public void listFolderNames() {
        String sourcePath = readText("input your sourcePath file/folder \n is absolute path or relative path include parent directory and file name starting from project root to rename: ");
        Set<String> FolderNames = FileFolderLib.listFolderNames(sourcePath);
        FolderNames.forEach(name -> System.out.println("+ " + name));
    }

    @Override
    public void listFilesDetail() {

    }

    @Override
    public void mergeFileData() {
        String sourcePath = readText("input your sourcePath file/folder \n is absolute path or relative path include parent directory and file name starting from project root to merge: ");
        String targetPath = readText("input your targetPath file/folder \n is absolute path or relative path include parent directory and file name starting from project root to merge: ");
        boolean isMerge = FileFolderLib.mergeFileData(sourcePath, targetPath);
        System.out.println("respond status: " + isMerge);
    }

    @Override
    public void appendFileContent() {
        String newData = readText("input your new data to append to source file: ");
        String sourcePath = readText("input your sourcePath file/folder \n is absolute path or relative path include parent directory and file name starting from project root to append new data: ");
        boolean isAppend = FileFolderLib.appendFileContent(newData, sourcePath);
        System.out.println("respond status: " + isAppend);
    }

    @Override
    public void readFileData() {
        String sourcePath = readText("input your sourcePath file/folder \n is absolute path or relative path include parent directory and file name starting from project root to read: ");
        String data = FileFolderLib.readFileData(sourcePath);
        System.out.println("respond data: " + data);
    }

    @Override
    public void writeDataToFile() {
        String newData = readText("input your new data to append to source file: ");
        String sourcePath = readText("input your sourcePath file/folder \n is absolute path or relative path include parent directory and file name starting from project root to write: ");
        boolean isWrite = FileFolderLib.writeDataToFile(newData, sourcePath);
        System.out.println("respond status: " + isWrite);
    }

    @Override
    public void overWriteFile() {
        String newData = readText("input your new data to append to source file: ");
        String sourcePath = readText("input your sourcePath file/folder \n is absolute path or relative path include parent directory and file name starting from project root to over write: ");
        boolean isWrite = FileFolderLib.writeDataToFile(newData, sourcePath);
        System.out.println("respond status: " + isWrite);
    }

    @Override
    public void fileFolderSize() {
        String sourcePath = readText("input your sourcePath file/folder \n is absolute path or relative path include parent directory and file name starting from project root to get file/folder size: ");
        String size = FileFolderLib.getFileFolderSize(sourcePath);
        System.out.println(sourcePath + " size: " + size);
    }

    @Override
    public void fileFolderAttributes() {
        String sourcePath = readText("input your sourcePath file/folder \n is absolute path or relative path include parent directory and file name starting from project root to get file/folder size: ");
        FileFolderLib.getFileFolderAttribute(sourcePath);
    }

    @Override
    public void deleteFileFolder() {
        String sourcePath = readText("input your sourcePath to delete file/folder \n is absolute path or relative path include parent directory and file name starting from project root: ");
        boolean isRenamed = FileFolderLib.deleteFileFolder(sourcePath);
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
