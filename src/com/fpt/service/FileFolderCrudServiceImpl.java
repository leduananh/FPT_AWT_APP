package com.fpt.service;

import com.fpt.folderHandleLib.FileFolderLib;

import java.util.Scanner;
import java.util.regex.Pattern;

public class FileFolderCrudServiceImpl implements FileFolderCrudService {
    private final Scanner scanner = new Scanner(System.in);

    @Override
    public void createFileFolder() {
        String sourcePath = readText("input your sourcePath file/folder \n is absolute path or relative path include parent directory and file name starting from project root to create: ");
        boolean isCreated = FileFolderLib.createFileFolder(sourcePath);
        System.out.println("respond status: " + isCreated);
        pressToContinue("press ENTER to back to main menu...");
    }

    @Override
    public void moveFileFolder() {
        String sourcePath = readText("input your sourcePath file/folder \n is absolute path or relative path include parent directory and file name starting from project root: ");
        String targetPath = readText("input your targetPath file/folder \n is absolute path or relative path include parent directory and file name starting from project root: ");
        boolean isMoved = FileFolderLib.moveFileFolder(sourcePath, targetPath);
        System.out.println("respond status: " + isMoved);
        pressToContinue("press ENTER to back to main menu...");
    }

    @Override
    public void checkFileFolderExist() {
        String sourcePath = readText("input your sourcePath file/folder \n is absolute path or relative path include parent directory and file name starting from project root to checking: ");
        boolean isExist = FileFolderLib.checkFileFolderExists(sourcePath);
        System.out.println("respond status: " + isExist);
        pressToContinue("press ENTER to back to main menu...");
    }

    @Override
    public void renameFileFolder() {
        String sourcePath = readText("input your sourcePath file/folder \n is absolute path or relative path include parent directory and file name starting from project root to rename: ");
        String newName = readText("input your new file/folder name: ");
        boolean isRenamed = FileFolderLib.renameFileFolder(sourcePath, newName);
        System.out.println("respond status: " + isRenamed);
        pressToContinue("press ENTER to back to main menu...");
    }

    @Override
    public void listFileFolderNames() {
        String sourcePath = readText("input your sourcePath file/folder \n is absolute path or relative path include parent directory and file name starting from project root to rename: ");
        String fileFolderNamesJson = FileFolderLib.listFileFolderNames(sourcePath);
        System.out.println("files/directories name array json: \n" + fileFolderNamesJson);
        pressToContinue("press ENTER to back to main menu...");
    }

    @Override
    public void listFileNames() {
        String sourcePath = readText("input your sourcePath file/folder \n is absolute path or relative path include parent directory and file name starting from project root to rename: ");
        String fileNamesJson = FileFolderLib.listFileNames(sourcePath);
        System.out.println("files name array json: \n" + fileNamesJson);
        pressToContinue("press ENTER to back to main menu...");
    }

    @Override
    public void listFolderNames() {
        String sourcePath = readText("input your sourcePath file/folder \n is absolute path or relative path include parent directory and file name starting from project root to rename: ");
        String folderNamesJson = FileFolderLib.listFolderNames(sourcePath);
        System.out.println("subdirectories name array json: \n" + folderNamesJson);
        pressToContinue("press ENTER to back to main menu...");
    }

    @Override
    public void listFileFolderAttributes() {
        String sourcePath = readText("input your sourcePath file/folder \n is absolute path or relative path include parent directory and file name starting from project root to rename: ");
        String json = FileFolderLib.listFileFolderAttributes(sourcePath);
        System.out.println("list attributes : " + json);
        pressToContinue("press ENTER to back to main menu...");
    }

    @Override
    public void mergeFileData() {
        String sourcePath = readText("input your sourcePath file/folder \n is absolute path or relative path include parent directory and file name starting from project root to merge: ");
        String targetPath = readText("input your targetPath file/folder \n is absolute path or relative path include parent directory and file name starting from project root to merge: ");
        boolean isMerge = FileFolderLib.mergeFileData(sourcePath, targetPath);
        System.out.println("respond status: " + isMerge);
        pressToContinue("press ENTER to back to main menu...");
    }

    @Override
    public void appendFileContent() {
        String newData = readText("input your new data to append to source file: ");
        String sourcePath = readText("input your sourcePath file/folder \n is absolute path or relative path include parent directory and file name starting from project root to append new data: ");
        boolean isAppend = FileFolderLib.appendFileContent(newData, sourcePath);
        System.out.println("respond status: " + isAppend);
        pressToContinue("press ENTER to back to main menu...");
    }

    @Override
    public void readFileData() {
        String sourcePath = readText("input your sourcePath file/folder \n is absolute path or relative path include parent directory and file name starting from project root to read: ");
        String data = FileFolderLib.readFileData(sourcePath);
        System.out.println("respond data: " + data);
        pressToContinue("press ENTER to back to main menu...");
    }

    @Override
    public void writeDataToFile() {
        String newData = readText("input your new data to append to source file: ");
        String sourcePath = readText("input your sourcePath file/folder \n is absolute path or relative path include parent directory and file name starting from project root to write: ");
        boolean isWrite = FileFolderLib.writeDataToFile(newData, sourcePath);
        System.out.println("respond status: " + isWrite);
        pressToContinue("press ENTER to back to main menu...");
    }

    @Override
    public void overWriteFile() {
        String newData = readText("input your new data to append to source file: ");
        String sourcePath = readText("input your sourcePath file/folder \n is absolute path or relative path include parent directory and file name starting from project root to over write: ");
        boolean isWrite = FileFolderLib.writeDataToFile(newData, sourcePath);
        System.out.println("respond status: " + isWrite);
        pressToContinue("press ENTER to back to main menu...");
    }

    @Override
    public void fileFolderSize() {
        String sourcePath = readText("input your sourcePath file/folder \n is absolute path or relative path include parent directory and file name starting from project root to get file/folder size: ");
        String size = FileFolderLib.getFileFolderSize(sourcePath);
        System.out.println(sourcePath + " size: " + size);
        pressToContinue("press ENTER to back to main menu...");
    }

    @Override
    public void fileFolderAttributes() {
        String sourcePath = readText("input your sourcePath file/folder \n is absolute path or relative path include parent directory and file name starting from project root to get file/folder size: ");
        String attributesJson = FileFolderLib.fileFolderAttributes(sourcePath);
        System.out.println("attributes: \n" + attributesJson);
        pressToContinue("press ENTER to back to main menu...");
    }

    @Override
    public void fileFolderCreationDate() {
        String sourcePath = readText("input your sourcePath file/folder \n is absolute path or relative path include parent directory and file name starting from project root to get creation date: ");
        String date = FileFolderLib.getFileFolderCreationDate(sourcePath);
        System.out.println("creation date: " + date);
        pressToContinue("press ENTER to back to main menu...");
    }

    @Override
    public void fileFolderLastModifiedDate() {
        String sourcePath = readText("input your sourcePath file/folder \n is absolute path or relative path include parent directory and file name starting from project root to get last modified date: ");
        String date = FileFolderLib.getFileFolderLastModifiedDate(sourcePath);
        System.out.println("last modified date: " + date);
        pressToContinue("press ENTER to back to main menu...");
    }

    @Override
    public void fileFolderLastAccessDate() {
        String sourcePath = readText("input your sourcePath file/folder \n is absolute path or relative path include parent directory and file name starting from project root to get last access date: ");
        String date = FileFolderLib.getFileFolderLastAccessDate(sourcePath);
        System.out.println("last access date: " + date);
        pressToContinue("press ENTER to back to main menu...");
    }

    @Override
    public void overWriteFileData() {
        String newData = readText("input your new data to append to source file: ");
        String sourcePath = readText("input your sourcePath file/folder \n is absolute path or relative path include parent directory and file name starting from project root to over write file: ");
        boolean status = FileFolderLib.overWriteFileData(newData, sourcePath);
        System.out.println("status: " + status);
        pressToContinue("press ENTER to back to main menu...");
    }

    @Override
    public void fileHasKeyword() {
        String keyword = readText("input your keyword to checking: ");
        String sourcePath = readText("input your sourcePath file \n is absolute path or relative path include parent directory and file name starting from project root to check keyword exist: ");
        boolean status = FileFolderLib.fileHasKeyword(keyword, sourcePath);
        System.out.println("is exist: " + status);
        pressToContinue("press ENTER to back to main menu...");
    }

    @Override
    public void fileDataAtRowIndex() {
        int rowIndex = readInt("input your row index: ");
        String sourcePath = readText("input your sourcePath file \n is absolute path or relative path include parent directory and file name starting from project root to get row data: ");
        String rowData = FileFolderLib.fileDataAtRowIndex(rowIndex, sourcePath);
        System.out.println("row data: " + rowData);
        pressToContinue("press ENTER to back to main menu...");
    }

    @Override
    public void fileReplaceAll() {
        String keyword = readText("input your search keyword: ");
        String replaceKeyword = readText("input your replace keyword: ");
        String sourcePath = readText("input your sourcePath file \n is absolute path or relative path include parent directory and file name starting from project root to get row data: ");
        boolean isReplaced = FileFolderLib.fileReplaceAll(keyword, replaceKeyword, sourcePath);
        System.out.println(" is Replaced: " + isReplaced);
        pressToContinue("press ENTER to back to main menu...");
    }

    @Override
    public void deleteFileFolder() {
        String sourcePath = readText("input your sourcePath to delete file/folder \n is absolute path or relative path include parent directory and file name starting from project root: ");
        boolean isRenamed = FileFolderLib.deleteFileFolder(sourcePath);
        System.out.println("respond status: " + isRenamed);
        pressToContinue("press ENTER to back to main menu...");
    }

    private void pressToContinue(String title) {
        System.out.println("\n" + title);
        scanner.nextLine();
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

    private int readInt(String request) {
        int input = 0;
        while (true) {
            try {
                printRequest(request);
                String data = scanner.nextLine();
                input = Integer.parseInt(data);
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
